;;;; Genifer/tests.lisp
;;;;
;;;; Copyright (C) 2009 Genint
;;;; All Rights Reserved
;;;;
;;;; Written by YKY
;;;;
;;;; This program is free software; you can redistribute it and/or modify
;;;; it under the terms of the GNU Affero General Public License v3 as
;;;; published by the Free Software Foundation and including the exceptions
;;;; at http://opencog.org/wiki/Licenses
;;;;
;;;; This program is distributed in the hope that it will be useful,
;;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;;; GNU General Public License for more details.
;;;;
;;;; You should have received a copy of the GNU Affero General Public License
;;;; along with this program; if not, write to:
;;;; Free Software Foundation, Inc.,
;;;; 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

;;;; ==========================================================
;;;; ***** System tests

;;; **** Initialize memories for tests
;;; Add the facts and rules
;;; This function must be called from Java
(defun init-test-mem (param)
  (setf class            (jclass "genifer.SimpleMemory")
        cons-class       (jclass "org.armedbear.lisp.Cons")
        floatclass       (jclass "float")
        method-addFact   (jmethod class "addFact" cons-class cons-class)
        method-addRule   (jmethod class "addRule" cons-class floatclass))

  (defun add-fact-to-memJ (formula &optional tv justifies justified-by)
    (add-fact-to-mem method-addFact param formula tv justifies justified-by))

  (defun add-rule-to-memJ (formula &optional w e+ e- ancestors ancestor-to)
    (add-rule-to-mem method-addRule param formula w e+ e- ancestors ancestor-to))

  (format t ";; Adding knowledge to KB.... ~%")

  ;;; the format for facts is:
  ;;;   (add-fact-to-mem (head) (TV) (justifies) (justified-by))
  ;;;   where
  ;;;      (TV) = (truth value . confidence)

  ;;; the format for rules is:
  ;;;   (add-rule-to-mem (head) (body) w+ w e+ e- ancestors ancestors-to)

  ;;; ************* Example 1
  ;;; This example occurs in this scenario:
  ;;;   "I ask Kellie out for a date, but she says she is busy. Then, I find her drinking
  ;;;   at the bar. Therefore, Kellie is lying."
  ;;; It contains the simple rules:
  ;;;   1. at_bar(x) -> having_fun(x)
  ;;;   2. busy(x) -> ! having_fun(x)
  ;;; and the following fact:
  ;;;   busy(kellie)
  ;;; The query would be:
  ;;;   (at-bar kellie) ?
  ;;; And the answer would be false (or its equivalent numerical truth value)

  (add-fact-to-memJ '(busy kellie) '(0.7 . 1.0))

  (add-rule-to-memJ '(<- (having-fun ?1) (Z-NOT (busy ?1))))
  (add-rule-to-memJ '(<- (having-fun ?1) (Z-MOD2 (at-bar ?1) 0.5 5.0)))

  ;;; ************* Example 2
  ;;; Just a 6-layer-deep search problem with a distracting branch
  ;;; Query:  (goal robot) ?
  (add-fact-to-memJ '(do-a robot) '(0.6 . 1.0))

  (add-rule-to-memJ '(<- (do-b ?1) (Z-MOD2 (do-a ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-c ?1) (Z-MOD2 (do-b ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-d ?1) (Z-MOD2 (do-c ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-e ?1) (Z-MOD2 (do-d ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-f ?1) (Z-MOD2 (do-e ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (goal ?1) (Z-MOD2 (do-f ?1) 0.5 5.0)))
  ; let's add a little distraction:
  (add-rule-to-memJ '(<- (do-x ?1) (Z-MOD2 (do-w ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-y ?1) (Z-MOD2 (do-x ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (do-z ?1) (Z-MOD2 (do-y ?1) 0.5 5.0)))
  (add-rule-to-memJ '(<- (goal ?1) (Z-MOD2 (do-z ?1) 0.5 5.0)))

  ;;; ************* Example 3
  ;;; c is a chair:
  ;;;     chair(X) <- leg(1,X) & leg(2,X) & leg(3,X) & leg(4,X) & seat(X) & back(X)
  ;;; Query:  (chair c) ?
  (add-fact-to-memJ '(leg 1 c) '(0.7 . 1.0))
  (add-fact-to-memJ '(leg 2 c) '(0.8 . 1.0))
  (add-fact-to-memJ '(leg 3 c) '(0.9 . 1.0))
  (add-fact-to-memJ '(leg 4 c) '(0.8 . 1.0))
  (add-fact-to-memJ '(seat  c) '(0.7 . 1.0))
  (add-fact-to-memJ '(back  c) '(0.9 . 1.0))

  (add-rule-to-memJ '(<- (chair ?1) (Z-AND (seat  ?1)
                                       (back  ?1)
                                       (leg 1 ?1)
                                       (leg 2 ?1)
                                       (leg 3 ?1)
                                       (leg 4 ?1))))

  ;;; A longer version using multiple intermediary predicates
  ;;; Query:  (chair2 c) ?
  (add-rule-to-memJ '(<- (chair2 ?1) (Z-AND (seat  ?1) (tmp4  ?1))))
  (add-rule-to-memJ '(<- (tmp4   ?1) (Z-AND (back  ?1) (tmp3  ?1))))
  (add-rule-to-memJ '(<- (tmp3   ?1) (Z-AND (leg 1 ?1) (tmp2  ?1))))
  (add-rule-to-memJ '(<- (tmp2   ?1) (Z-AND (leg 2 ?1) (tmp1  ?1))))
  (add-rule-to-memJ '(<- (tmp1   ?1) (Z-AND (leg 3 ?1) (leg 4 ?1))))

  ;;; ************* Example 4
  ;;; From Luger's AI textbook, 2009
  ;;; Query:  (happy john) ?

  ;;; Anyone passing his history exams and winning the lottery is happy:
  (add-rule-to-memJ '(<- (happy ?1) (Z-AND (pass ?1 history) (win ?1 lottery))))

  ;;; Anyone who studies or is lucky can pass all his exams:
  (add-rule-to-memJ '(<- (pass ?1 ?2) (Z-OR (study ?1) (lucky ?1))))

  ;;; John did not study but is lucky:
  (add-fact-to-memJ '(study john) '(0.0 . 1.0))
  (add-fact-to-memJ '(lucky john))

  ;;; Anyone who is lucky wins the lottery:
  (add-rule-to-memJ '(<- (win ?1 lottery) (ID (lucky ?1))))

  ;;; ************* Example 5
  ;;; Test handling of function symbols, ie unification.
  ;;; Query:  (grandparent john ?1)

  ;;; This is an example of a body-less rule:
  ;;; parent(X, son-of(X)) <-
  (add-rule-to-memJ '(<- (parent ?1 (son-of ?1))))

  ;;; ~parent(W,Y) \/ ~parent(Y,Z) \/ grandparent(W,Z)
  (add-rule-to-memJ '(<- (grandparent ?1 ?3) (Z-AND (parent ?1 ?2) (parent ?2 ?3))))

  ;;; ************* Example 6
  ;;; Test of variable binding across a conjunction.
  ;;;     p(X,Y) <- q(X,Z) /\ r(Z,Y)

  ;;;     grandpa(X,Y) <- pa(X,Z) /\ pa(Z,Y)
  ;;;     grandpa(X,Y) <- pa(X,Z) /\ ma(Z,Y)
  ;;;     pa(john,pete)
  ;;;     pa(pete,paul)
  ;;;     ma(mary,paul)
  ;;;     pa(john,mary)
  ;;; Query: grandpa(john,paul)?
  ;;; Query: grandpa(john,sam)?

  (add-rule-to-memJ '(<- (grandpa ?1 ?2) (Z-AND (pa ?1 ?3) (pa ?3 ?2))))
  (add-rule-to-memJ '(<- (grandpa ?1 ?2) (Z-AND (pa ?1 ?3) (ma ?3 ?2))))
  (add-fact-to-memJ '(pa john pete) '(1.0 . 1.0))
  (add-fact-to-memJ '(pa pete paul) '(1.0 . 1.0))
  (add-fact-to-memJ '(ma mary sam)  '(1.0 . 1.0))
  (add-fact-to-memJ '(pa john mary) '(1.0 . 1.0))

  ;;; ************* Example 7
  ;;; This background knowledge is used for testing induction in "induction1.lisp"
  ;;; Query: (has-dau pam) ?   **** should return false

  ;(add-rule-to-memJ '(<- (has-dau ?1) (Z-AND (parent ?1 ?2) (female ?2))))

  (add-fact-to-memJ '(parent pam bob))
  (add-fact-to-memJ '(parent tom bob))
  (add-fact-to-memJ '(parent tom liz))
  (add-fact-to-memJ '(parent bob ann))
  (add-fact-to-memJ '(parent bob pat))
  (add-fact-to-memJ '(parent pat jim))
  (add-fact-to-memJ '(parent pat eve))
  ;(add-fact-to-memJ '(parent juu luu))

  (add-fact-to-memJ '(female pam))
  (add-fact-to-memJ '(male tom))
  (add-fact-to-memJ '(male bob))
  (add-fact-to-memJ '(female liz))
  (add-fact-to-memJ '(female ann))
  (add-fact-to-memJ '(female pat))
  (add-fact-to-memJ '(male jim))
  (add-fact-to-memJ '(female eve))
  ;(add-fact-to-memJ '(male juu))
  ;(add-fact-to-memJ '(male luu))

  (format t ";; Working Memory initialized... ~%"))

(defvar *method-findAllFacts*)
(defvar *method-findAllRules*)

;;; Perform system tests
(defun system-test ()
  (setf class                 (jclass "genifer.SimpleMemory")
        string-class          (jclass "String")
        *method-findAllFacts* (jmethod class "findAllFacts" string-class)
        *method-findAllRules* (jmethod class "findAllRules" string-class))
  (setf *abduce* nil)
  (setf *debug-level* 10)
  (format t "Time elapsed: ~1,15@T expected TV ~1,30@T confidence ~1,45@T substitutions~%")
  ; format:  query expected-tv expected-sub
  (test-1-query '(having-fun kellie) '(0.3 . 0.9) nil)         ; 1
  (test-1-query '(at-bar kellie) '(0.3 . 0.9) nil)             ; 1 **** not yet ready
  (test-1-query '(goal robot) '(0.9 . 0.9) nil)                ; 2
  (test-1-query '(goal robot2) 'fail nil)                      ; 2
  (test-1-query '(chair c) '(0.7 . 0.9) nil)                   ; 3
  (test-1-query '(chair c2) 'fail nil)                         ; 3
  (test-1-query '(chair2 c) '(0.7 . 0.9) nil)                  ; 3b
  (test-1-query '(chair2 c2) 'fail nil)                        ; 3b
  (test-1-query '(happy john) '(1.0 . 0.9) nil)                ; 4
  ; 5
  (test-1-query '(grandparent john ?99) '(1.0 . 0.9) '(?99 . (son-of (son-of john))))
  (test-1-query '(grandparent john ?1) '(1.0 . 0.9) '(?99 . (son-of (son-of john))))
  (test-1-query '(grandparent ?1 ?2) '(1.0 . 0.9) '())
  (test-1-query '(grandparent ?1 john) 'fail nil)
  ; 6
  (test-1-query '(grandpa john paul) '(1.0 . 0.9) '(?a . pete))
  (test-1-query '(grandpa john sam) '(1.0 . 0.9) '(?b . mary))
  (test-1-query '(grandpa ?1 sam) '(1.0 . 0.9) '((?1 . john) (?a . mary)))
  (setf *debug-level* 1)
)


(defun test-1-query (query &optional expected-tv expected-sub)
  (setf timer (get-internal-run-time))
  (backward-chain query)
  (setf solutions (solutions proof-tree))
  (if (equal 'fail solutions)
    (setf s1 nil)
    (setf s1 (car solutions)))
  (if (null s1)
    (if (equal 'fail expected-tv)
      (setf tv-accuracy         100.0
            confidence-accuracy 100.0)
      (setf tv-accuracy         0.0
            confidence-accuracy 0.0))
    (if (equal 'fail expected-tv)
      (setf tv-accuracy         0.0
            confidence-accuracy 0.0)
      (progn
        (setf tv-accuracy         (* 100.0 (- 1.0 (- (car (tv s1)) (car expected-tv)))))
        (setf confidence-accuracy (* 100.0 (- 1.0 (- (cdr (tv s1)) (cdr expected-tv))))))))
  (format t "~aus ~1,15@T ~a% ~1,30@T ~a% ~%" (- (get-internal-run-time) timer)
                     tv-accuracy confidence-accuracy))
