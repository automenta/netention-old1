;;;; Genifer/memory.lisp
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
;;;; ***** Memory systems
;;;; Currently the memory is just a simple list of all items, stored in *generic-memory*.

;;; **** Data structure for a Generic-Memory "fact" item
;;; Entries:
;;;     tv           = truth value and confidence
;;;     justifies    = a list of clauses that this fact justifies
;;;     justified-by = a list of clauses that justify this fact
;;; timestamp -- we should not timestamp for every clause, use time-markers instead

;;; **** Data structure for a Generic-Memory "rule" item
;;; Entries:
;;;     w            = size of support (ie, total number of times the rule is involved in proofs)
;;;     e+           = positive examples
;;;     e-           = negative examples
;;;     ancestors    = a list of ancestor rules of this rule
;;;     ancestors-to = a list of rules that this rule is ancestor to

;;; Definition of the truth-value "T"
(defparameter *true* '(1.0 . 1.0))

(defvar *generic-memory*)
(defvar *memory-size*)
(defvar *newly-added*)
(defvar new-rule)

;;; **** Initialize memories
;;; Just create the empty KB and return
(defun init-memories ()
  ;; Generic Memory
  (setf *generic-memory* nil)
  ;; index
  (setf *memory-size* 0)
  ;; counter used in batch processing
  (setf *newly-added* 0))


;;; **** Add a clause to memory
;;; TODO:  label, and the stack from abduction
(defun add-to-memory (clause label)
  ;; check if the clause is already in memory -- this may be time-consuming.
  (dolist (item *generic-memory*)
    (if (equal clause (slot-value item 'clause))
      (return-from add-to-memory)))     ; if so, exit
  ;; if not in memory, add it
  (if (is-ground clause)
    (add-fact-to-memJ clause)
    (add-rule-to-memJ clause)))

(defvar new-fact)

;;; **** Add a fact to Generic Memory
;;; The last 2 parameter are unused as of this version
(defun add-fact-to-mem (method param formula &optional tv justifies justified-by)
  (if (null tv)
    (setf tv (cons 1.0 1.0)))          ; default TV
  (****DEBUG 1 "adding fact to memory: ~a" formula)
  (****DEBUG 1 "tv is: ~a" tv)
  (jcall method param formula tv))

;;; **** Add a rule to Generic Memory
;;; The last 4 parameters are unused as of this version
(defun add-rule-to-mem (method param formula &optional w e+ e- ancestors ancestor-to)
  ;; Set default values:
  (****DEBUG 1 "adding rule to memory: ~a <- ~a" (second formula) (third formula))
  ;(if (null body) (setf body '(*bodyless*)))
  (if (null w )   (setf w    100.0))
  (if (null e+)   (setf e+   0))
  (if (null e-)   (setf e-   0))
  (jcall method param formula w))

(defun delete-memory-item (item)
  (setf ptr *generic-memory*)
  ;; Special case:
  (if (eql item ptr)
    (return-from delete-memory-item
      (setf *generic-memory* (cdr *generic-memory*))))
  ;; Otherwise, find the item
  (loop
    (if (eql item (cdr ptr)) (return))
    (setf ptr (cdr ptr)))
  ;; Delete it
  (setf (cdr ptr) (cdr item)))

;;; Fetch all clauses in KB with the given head-predicate
;;; Return: a list of rules
(defun fetch-clauses (head-predicate)
  (let ((facts-list (list nil))
        (rules-list (list nil)))
    (dolist (item *generic-memory*)
      ;; is it a rule?
      (if (eql (type-of item) 'rule-item)
        (let ((head (head item))
              (body (body item)))
          ;; Does head of rule match head-predicate?
          (if (equal (car head) head-predicate)
            (progn
              ;; calculate the confidence c from w
              ;; the function is defined in "PZ-calculus.lisp"
              (setf confidence (convert-w-2-c (w item)))
              ;; add it to list-to-be-returned
              (nconc rules-list (list (make-instance 'clause
                                        :id         (id item)
                                        :confidence confidence
                                        :head       head
                                        :body       body))))))
        ;; If it is a fact:
        ;; Does fact match head-predicate?
        (if (equal (car (fact item)) head-predicate)
          (let ((tv (tv item)))
            ;; add it to list-to-be-returned
            (nconc facts-list (list (make-instance 'clause
                                      :id         (id item)
                                      :confidence (cdr tv)
                                      :head       (fact item)
                                      :tv         tv)))))))
    ;; return the 2 lists, discarding the leading 'nil' items
    (values (cdr facts-list) (cdr rules-list))))

;;; Comparison predicate for "sort" in function "fetch-clauses"
;;; should return true iff x1 is strictly less than x2
;;; if x1 is greater than or equal to x2, return false
;;; sort seems to order from small to big -- we need to reverse this -- biggest confidence 1st
;;; each element is a list:  (confidence head body)
(defun compare-items (x1 x2)
  (> (car x1) (car x2)))

;;; This function is used in natural-language.lisp
(defvar *entity-counter* 1)
;;; **** Creates a new entity
(defun new-entity ()
  (incf *entity-counter*))

;;; ------------------------ miscellaneous functions -------------------------

;;; **** Print out memory contents
(defun dump-memory ()
  (dolist (item *generic-memory*)
    (if (eql (type-of item) 'fact-item)
      ;; print a fact item
      (progn
        (format t "**** [~a] fact: ~a ~%" (id item) (fact item))
        (setf tv (tv item))
        (format t "  TV:           ~a ~%" (car tv))
        (format t "  confidence:   ~a ~%" (cdr tv))
        (format t "  justifies:    ~a ~%" (justifies    item))
        (format t "  justified-by: ~a ~%" (justified-by item)))
      ;; print a rule item
      (progn
        (format t "**** [~a] rule: ~%"    (id          item))
        (format t "  head:         ~a ~%" (head        item))
        (format t "  body:         ~a ~%" (body        item))
        (format t "  w:            ~a ~%" (w           item))
        (format t "  e+:           ~a ~%" (e+          item))
        (format t "  e-:           ~a ~%" (e-          item))
        (format t "  ancestors:    ~a ~%" (ancestors   item))
        (format t "  ancestors-to: ~a ~%" (ancestor-to item))))))
