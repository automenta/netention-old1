/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

/**
 *
 * @author seh
 */
/******************************************************************************
 *                                                                             *
 *  Copyright: (widthIndexes) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
/*
** Derived from Public-Domain source as indicated at
** http://math.nist.gov/javanumerics/jama/ as of 9/13/2009.
*/

import com.syncleus.dann.math.linear.RealMatrix;
import com.syncleus.dann.math.linear.SimpleRealMatrix;


import java.io.Serializable;
import com.syncleus.dann.math.RealNumber;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * The Java MapRealMatrix Class provides the fundamental operations of
 * numerical linear algebra - using HashMap's for sparse matrices.
 * <p/>
 * Five fundamental matrix decompositions, which consist of pairs or triples of
 * matrices, permutation vectors, and the like, produce results in five
 * decomposition classes.  These decompositions are accessed by the
 * SimpleRealMatrix class to compute solutions of simultaneous linear equations,
 * determinants, inverses and other matrix functions.  The five decompositions
 * are: <P><UL> <LI>Cholesky Decomposition of symmetric, positive definite
 * matrices. <LI>LU Decomposition of rectangular matrices. <LI>QR Decomposition
 * of rectangular matrices. <LI>Singular Value Decomposition of rectangular
 * matrices. <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric
 * square matrices. </UL>
 */
public class MapRealMatrix extends SimpleRealMatrix implements Serializable, RealMatrix
{
	private static final Logger LOGGER = Logger.getLogger(SimpleRealMatrix.class);
	/**
	 * Array for internal storage of elements.
	 */
    private Map<Integer, Map<Integer, Double>> matrixElements;
    
	/**
	 * Row and column dimensions.
	 */
	private final int mheight;
	private final int mwidth;
    private double defaultValue;

	/**
	 * Construct an height-by-height matrix of zeros.
	 *
	 * @param height Number of rows.
	 * @param width Number of colums.
	 */
	public MapRealMatrix(final int height, final int width)
	{
        super(1, 1);
		this.mheight = height;
		this.mwidth = width;
		this.matrixElements = new HashMap();
	}

    @Override
    public int getWidth() {
        return mwidth;
    }

    @Override
    public int getHeight() {
        return mheight;
    }


	/**
	 * Construct an height-by-width constant matrix.
	 *
	 * @param height Number of rows.
	 * @param width Number of colums.
	 * @param defaultValue Fill the matrix with this scalar value.
	 */
	public MapRealMatrix(final int height, final int width, final double defaultValue)
	{
        this(height, width);
        this.defaultValue = defaultValue;
	}

//	/**
//	 * Multiply a matrix by a scalar, resultArray = scalar*matrixElements
//	 *
//	 * @param scalar scalar
//	 * @return scalar*matrixElements
//	 */

	/**
	 * Get a single element.
	 *
	 * @param heightIndex Row index.
	 * @param widthIndex Column index.
	 * @return value at the specified element
	 */
	public double getDouble(final int heightIndex, final int widthIndex)
	{
        //return this.matrixElements[heightIndex][widthIndex];
        Map<Integer, Double> row = matrixElements.get(heightIndex);
        if (row != null) {
            Double value = row.get(widthIndex);
            if (value!=null)
                return value;
        }

        return defaultValue;
	}

	public RealMatrix set(final int heightIndex, final int widthIndex, final RealNumber fillValue)
	{
        Map<Integer, Double> row = matrixElements.get(heightIndex);
        if (row == null) {
            row = new HashMap<Integer, Double>();
            matrixElements.put(heightIndex, row);
        }
        
        Double value = row.get(widthIndex);
        row.put(widthIndex, fillValue.getValue());

		return this;
	}

	/**
	 * Get a single element.
	 *
	 * @param heightIndex Row index.
	 * @param widthIndex Column index.
	 * @return RealNumber value of the specified element.
	 */
	public RealNumber get(final int heightIndex, final int widthIndex)
	{
		return new RealNumber(this.getDouble(heightIndex, widthIndex));
	}

	public RealMatrix blank()
	{
		return new MapRealMatrix(this.mheight, this.mwidth);
	}
//
////	public RealMatrix flip()
////	{
////		final double[][] flippedSolution = new double[this.width][this.height];
////		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
////			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
////				flippedSolution[widthIndex][heightIndex] = this.matrixElements[heightIndex][widthIndex];
////		return new SimpleRealMatrix(flippedSolution);
////	}
//
//	/**
//	 * Get a submatrix.
//	 *
//	 * @param heightStart Initial row index
//	 * @param heightEnd Final row index
//	 * @param widthStart Initial column index
//	 * @param widthEnd Final column index
//	 * @return matrixElements(heightStart:heightEnd,widthStart:widthEnd)
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public RealMatrix getSubmatrix(final int heightStart, final int heightEnd, final int widthStart, final int widthEnd)
//	{
//		final double[][] subMatrix = new double[heightEnd - heightStart + 1][widthEnd - widthStart + 1];
//		for(int heightIndex = heightStart; heightIndex <= heightEnd; heightIndex++)
//			for(int widthIndex = widthStart; widthIndex <= widthEnd; widthIndex++)
//				subMatrix[heightIndex - heightStart][widthIndex - widthStart] = this.matrixElements[heightIndex][widthIndex];
//		return new SimpleRealMatrix(subMatrix);
//	}
//
//	/**
//	 * Get a submatrix.
//	 *
//	 * @param heightIndexes Array of row indices.
//	 * @param widthIndexes Array of column indices.
//	 * @return matrixElements(heightIndexes(:),widthIndexes(:))
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public RealMatrix getSubmatrix(final int[] heightIndexes, final int[] widthIndexes)
//	{
//		//SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(heightIndexes.length, widthIndexes.length);
//		//double[][] newMatrix = newSimpleMatrix.getArray();
//		final double[][] newMatrix = new double[heightIndexes.length][widthIndexes.length];
//		for(int heightIndex = 0; heightIndex < heightIndexes.length; heightIndex++)
//			for(int widthIndex = 0; widthIndex < widthIndexes.length; widthIndex++)
//				newMatrix[heightIndex][widthIndex] = this.matrixElements[heightIndexes[heightIndex]][widthIndexes[widthIndex]];
//		return new SimpleRealMatrix(newMatrix);
//	}
//
//	/**
//	 * Get a submatrix.
//	 *
//	 * @param heightStart Initial row index
//	 * @param heightEnd Final row index
//	 * @param widthIndexes Array of column indices.
//	 * @return matrixElements(heightStart:heightEnd,widthIndexes(:))
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public RealMatrix getSubmatrix(final int heightStart, final int heightEnd, final int[] widthIndexes)
//	{
////		SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(heightEnd - heightStart + 1, widthIndexes.length);
////		double[][] newMatrix = newSimpleMatrix.getArray();
//		final double[][] newMatrix = new double[heightEnd - heightStart + 1][widthIndexes.length];
//		for(int heightIndex = heightStart; heightIndex <= heightEnd; heightIndex++)
//			for(int widthIndex = 0; widthIndex < widthIndexes.length; widthIndex++)
//				newMatrix[heightIndex - heightStart][widthIndex] = this.matrixElements[heightIndex][widthIndexes[widthIndex]];
//		return new SimpleRealMatrix(newMatrix);
//	}
//
//	/**
//	 * Get a submatrix.
//	 *
//	 * @param heightIndexes Array of row indices.
//	 * @param widthStart Initial column index
//	 * @param widthEnd Final column index
//	 * @return A RealMatrix represented the elements specified
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public RealMatrix getSubmatrix(final int[] heightIndexes, final int widthStart, final int widthEnd)
//	{
//		final double[][] newMatrix = new double[heightIndexes.length][widthEnd - widthStart + 1];
//		for(int heightIndex = 0; heightIndex < heightIndexes.length; heightIndex++)
//			for(int widthIndex = widthStart; widthIndex <= widthEnd; widthIndex++)
//				newMatrix[heightIndex][widthIndex - widthStart] = this.matrixElements[heightIndexes[heightIndex]][widthIndex];
//		return new SimpleRealMatrix(newMatrix);
//	}
//
//	/**
//	 * Set a single element.
//	 *
//	 * @param heightIndex Row index.
//	 * @param widthIndex Column index.
//	 * @param fillValue value to set
//	 */
//	public RealMatrix set(final int heightIndex, final int widthIndex, final RealNumber fillValue)
//	{
//		final double[][] copy = this.toDoubleArray();
//		copy[heightIndex][widthIndex] = fillValue.getValue();
//		return new SimpleRealMatrix(copy);
//	}
//
//	/**
//	 * Set a submatrix.
//	 *
//	 * @param heightStart Initial row index
//	 * @param heightEnd Final row index
//	 * @param widthStart Initial column index
//	 * @param widthEnd Final column index
//	 * @param fillMatrix the source matrix to use to fill the specified elements of
//	 * this matrix.
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public void setMatrix(final int heightStart, final int heightEnd, final int widthStart, final int widthEnd, final SimpleRealMatrix fillMatrix)
//	{
//		for(int i = heightStart; i <= heightEnd; i++)
//			for(int j = widthStart; j <= widthEnd; j++)
//				this.matrixElements[i][j] = fillMatrix.getDouble(i - heightStart, j - widthStart);
//	}
//
//	/**
//	 * Set a submatrix.
//	 *
//	 * @param heightIndexes Array of row indices.
//	 * @param widthIndexes Array of column indices.
//	 * @param fillMatrix source matrix used to fill the specified elements.
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public void setMatrix(final int[] heightIndexes, final int[] widthIndexes, final SimpleRealMatrix fillMatrix)
//	{
//		for(int i = 0; i < heightIndexes.length; i++)
//			for(int j = 0; j < widthIndexes.length; j++)
//				this.matrixElements[heightIndexes[i]][widthIndexes[j]] = fillMatrix.getDouble(i, j);
//	}
//
//	/**
//	 * Set a submatrix.
//	 *
//	 * @param heightIndexes Array of row indices.
//	 * @param widthStart Initial column index
//	 * @param widthEnd Final column index
//	 * @param fillMatrix Source matrix used to fill the specified elements.
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public void setMatrix(final int[] heightIndexes, final int widthStart, final int widthEnd, final SimpleRealMatrix fillMatrix)
//	{
//		for(int i = 0; i < heightIndexes.length; i++)
//			for(int j = widthStart; j <= widthEnd; j++)
//				this.matrixElements[heightIndexes[i]][j] = fillMatrix.getDouble(i, j - widthStart);
//	}
//
//	/**
//	 * Set a submatrix.
//	 *
//	 * @param heightStart Initial row index
//	 * @param heightEnd Final row index
//	 * @param widthIndexes Array of column indices.
//	 * @param fillMatrix Source matrix used to fill the specified elements.
//	 * @throws ArrayIndexOutOfBoundsException Submatrix indices
//	 */
//	public void setMatrix(final int heightStart, final int heightEnd, final int[] widthIndexes, final SimpleRealMatrix fillMatrix)
//	{
//		for(int i = heightStart; i <= heightEnd; i++)
//			for(int j = 0; j < widthIndexes.length; j++)
//				this.matrixElements[i][widthIndexes[j]] = fillMatrix.getDouble(i - heightStart, j);
//	}
//
//	/**
//	 * SimpleRealMatrix transpose.
//	 *
//	 * @return matrixElements'
//	 */
//	public RealMatrix transpose()
//	{
//		final double[][] transposedMatrix = new double[width][height];
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				transposedMatrix[widthIndex][heightIndex] = this.matrixElements[heightIndex][widthIndex];
//		return new SimpleRealMatrix(transposedMatrix);
//	}
//
//	/**
//	 * One norm
//	 *
//	 * @return maximum column sum.
//	 */
//	public double norm1Double()
//	{
//		double f = 0;
//		for(int j = 0; j < this.width; j++)
//		{
//			double s = 0;
//			for(int i = 0; i < this.height; i++)
//				s += Math.abs(this.matrixElements[i][j]);
//			f = Math.max(f, s);
//		}
//		return f;
//	}
//
//	public RealNumber norm1()
//	{
//		return new RealNumber(this.norm1Double());
//	}
//
//	/**
//	 * Two norm
//	 *
//	 * @return maximum singular value.
//	 */
//	public double norm2Double()
//	{
//		return (new StewartSingularValueDecomposition(this).norm2Double());
//	}
//
//	public RealNumber norm2()
//	{
//		return new RealNumber(this.norm2Double());
//	}
//
//	/**
//	 * Infinity norm
//	 *
//	 * @return maximum row sum.
//	 */
//	public double normInfiniteDouble()
//	{
//		double f = 0;
//		for(int i = 0; i < this.height; i++)
//		{
//			double s = 0;
//			for(int j = 0; j < this.width; j++)
//				s += Math.abs(this.matrixElements[i][j]);
//			f = Math.max(f, s);
//		}
//		return f;
//	}
//
//	public RealNumber normInfinite()
//	{
//		return new RealNumber(this.normInfiniteDouble());
//	}
//
//	/**
//	 * Frobenius norm
//	 *
//	 * @return sqrt of sum of squares of all elements.
//	 */
//	public double normF()
//	{
//		double f = 0;
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				f = Math.hypot(f, this.matrixElements[i][j]);
//		return f;
//	}
//
//	/**
//	 * Unary subtract
//	 *
//	 * @return -matrixElements
//	 */
//	public RealMatrix negate()
//	{
//		//SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(height, width);
//		//double[][] negatedMatrix = newSimpleMatrix.getArray();
//		final double[][] negatedMatrix = new double[height][width];
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				negatedMatrix[heightIndex][widthIndex] = -this.matrixElements[heightIndex][widthIndex];
//		return new SimpleRealMatrix(negatedMatrix);
//	}
//
//	/**
//	 * resultArray = matrixElements + operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements + operand
//	 */
//	public RealMatrix add(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//
////		SimpleRealMatrix resultMatrix = new SimpleRealMatrix(height, width);
////		double[][] resultArray = resultMatrix.getArray();
//		final double[][] resultArray = new double[height][width];
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				resultArray[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] + operand.getDouble(heightIndex, widthIndex);
//		return new SimpleRealMatrix(resultArray);
//	}
//
//	/**
//	 * scalar addition matrixElements + operand
//	 *
//	 * @param operand scalar value to add to each element in this matrix.
//	 * @return a new element containing the result of this scalar addition.
//	 */
//	public RealMatrix add(final RealNumber operand)
//	{
//		final SimpleRealMatrix newSimpleMatrix = new SimpleRealMatrix(this.height, this.width);
//		final double[][] newMatrix = newSimpleMatrix.matrixElements;
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrix[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] + operand.getValue();
//		return newSimpleMatrix;
//	}
//
//	/**
//	 * scalar addition each element of this matrix added to scalar.
//	 *
//	 * @param scalar scalar value to add.
//	 * @return new matrix containing the result of this operation.
//	 */
//	public RealMatrix add(final double scalar)
//	{
//		final SimpleRealMatrix newMatrix = new SimpleRealMatrix(this.height, this.width);
//		final double[][] newMatrixElements = newMatrix.matrixElements;
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrixElements[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] + scalar;
//		return newMatrix;
//	}
//
//	/**
//	 * matrixElements = matrixElements + operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements + operand
//	 */
//	public RealMatrix addEquals(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final double[][] newMatrixElements = this.matrixElements.clone();
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrixElements[heightIndex][widthIndex] = newMatrixElements[heightIndex][widthIndex] + operand.getDouble(heightIndex, widthIndex);
//		return new SimpleRealMatrix(newMatrixElements);
//	}
//
//	/**
//	 * resultArray = matrixElements - operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements - operand
//	 */
//	public RealMatrix subtract(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final SimpleRealMatrix newMatrix = new SimpleRealMatrix(this.height, this.width);
//		final double[][] newMatrixElements = newMatrix.matrixElements;
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrixElements[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] - operand.getDouble(heightIndex, widthIndex);
//		return newMatrix;
//	}
//
//	public RealMatrix subtract(final RealNumber scalar)
//	{
//		return this.add(-1.0 * scalar.getValue());
//	}
//
//	public RealMatrix subtract(final double scalar)
//	{
//		return this.add(-1.0 * scalar);
//	}
//
//	/**
//	 * matrixElements = matrixElements - operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements - operand
//	 */
//	public RealMatrix subtractEquals(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final SimpleRealMatrix newMatrix = new SimpleRealMatrix(this.height, this.width);
//		final double[][] newMatrixElements = newMatrix.matrixElements;
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrixElements[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] - operand.getDouble(heightIndex, widthIndex);
//		return newMatrix;
//	}
//
//	/**
//	 * Element-by-element multiplication, resultArray = matrixElements.*operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements.*operand
//	 */
//	public RealMatrix arrayTimes(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final SimpleRealMatrix newMatrix = new SimpleRealMatrix(this.height, this.width);
//		final double[][] newMatrixElements = newMatrix.matrixElements;
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//				newMatrixElements[heightIndex][widthIndex] = this.matrixElements[heightIndex][widthIndex] * operand.getDouble(heightIndex, widthIndex);
//		return newMatrix;
//	}
//
//	/**
//	 * Element-by-element multiplication in place, matrixElements =
//	 * matrixElements.*operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements.*operand
//	 */
//	public RealMatrix arrayTimesEquals(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				this.matrixElements[i][j] = this.matrixElements[i][j] * operand.getDouble(i, j);
//		return this;
//	}
//
//	/**
//	 * Element-by-element right division, resultArray = matrixElements./operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements./operand
//	 */
//	public RealMatrix arrayRightDivide(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final SimpleRealMatrix X = new SimpleRealMatrix(this.height, this.width);
//		final double[][] C = X.matrixElements;
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				C[i][j] = this.matrixElements[i][j] / operand.getDouble(i, j);
//		return X;
//	}
//
//	/**
//	 * Element-by-element right division in place, matrixElements =
//	 * matrixElements./operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements./operand
//	 */
//	public RealMatrix arrayRightDivideEquals(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				this.matrixElements[i][j] = this.matrixElements[i][j] / operand.getDouble(i, j);
//		return this;
//	}
//
//	/**
//	 * Element-by-element left division, resultArray = matrixElements.\operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements.\operand
//	 */
//	public RealMatrix arrayLeftDivide(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		final SimpleRealMatrix X = new SimpleRealMatrix(this.height, this.width);
//		final double[][] C = X.matrixElements;
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				C[i][j] = operand.getDouble(i, j) / this.matrixElements[i][j];
//		return X;
//	}
//
//	/**
//	 * Element-by-element left division in place, matrixElements =
//	 * matrixElements.\operand
//	 *
//	 * @param operand another matrix
//	 * @return matrixElements.\operand
//	 */
//	public RealMatrix arrayLeftDivideEquals(final RealMatrix operand)
//	{
//		checkMatrixDimensions(operand);
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				this.matrixElements[i][j] = operand.getDouble(i, j) / this.matrixElements[i][j];
//		return this;
//	}
//
//
//	/**
//	 * Multiply a matrix by a scalar in place, matrixElements =
//	 * scalar*matrixElements
//	 *
//	 * @param scalar scalar
//	 * @return replace matrixElements by scalar*matrixElements
//	 */
//	public RealMatrix multiplyEquals(final double scalar)
//	{
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				this.matrixElements[i][j] = scalar * this.matrixElements[i][j];
//		return this;
//	}
//
//	public RealMatrix multiplyEquals(final RealNumber scalar)
//	{
//		for(int i = 0; i < this.height; i++)
//			for(int j = 0; j < this.width; j++)
//				this.matrixElements[i][j] = scalar.getValue() * this.matrixElements[i][j];
//		return this;
//	}
//
//	/**
//	 * Linear algebraic matrix multiplication, matrixElements * operand
//	 *
//	 * @param operand another matrix
//	 * @return SimpleRealMatrix product, matrixElements * operand
//	 * @throws IllegalArgumentException SimpleRealMatrix inner dimensions must
//	 * agree.
//	 */
//	public RealMatrix multiply(final RealMatrix operand)
//	{
//		if( operand.getHeight() != this.width )
//			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
//		final SimpleRealMatrix resultMatrix = new SimpleRealMatrix(this.height, operand.getWidth());
//		final double[][] resultArray = resultMatrix.matrixElements;
//		final double[] Bcolj = new double[width];
//		for(int j = 0; j < operand.getWidth(); j++)
//		{
//			for(int k = 0; k < this.width; k++)
//				Bcolj[k] = operand.getDouble(k, j);
//			for(int i = 0; i < this.height; i++)
//			{
//				final double[] Arowi = this.matrixElements[i];
//				double s = 0;
//				for(int k = 0; k < this.width; k++)
//					s += Arowi[k] * Bcolj[k];
//				resultArray[i][j] = s;
//			}
//		}
//		return resultMatrix;
//	}
//
//	/**
//	 * Solve matrixElements*resultMatrix = operand
//	 *
//	 * @param operand right hand side
//	 * @return solution if matrixElements is square, least squares solution
//	 *         otherwise
//	 */
//	public RealMatrix solve(final RealMatrix operand)
//	{
//		return (this.height == this.width ? (new DoolittleLuDecomposition<RealMatrix, RealNumber>(this)).solve(operand) : (new HouseholderQrDecomposition<RealMatrix, RealNumber>(this)).solve(operand));
//	}
//
//	/**
//	 * Solve resultMatrix*matrixElements = operand, which is also
//	 * matrixElements'*resultMatrix' = operand'
//	 *
//	 * @param operand right hand side
//	 * @return solution if matrixElements is square, least squares solution
//	 *         otherwise.
//	 */
//	public RealMatrix solveTranspose(final RealMatrix operand)
//	{
//		return this.transpose().solve(operand.transpose());
//	}
//
//	/**
//	 * SimpleRealMatrix reciprocal or pseudoinverse
//	 *
//	 * @return reciprocal(matrixElements) if matrixElements is square,
//	 *         pseudoinverse otherwise.
//	 */
//	public RealMatrix reciprocal()
//	{
//		return solve(identity(this.height, this.height));
//	}
//
//	/**
//	 * SimpleRealMatrix determinant
//	 *
//	 * @return determinant
//	 */
//	public double getDeterminant()
//	{
//		return new DoolittleLuDecomposition<RealMatrix, RealNumber>(this).getDeterminant().getValue();
//	}
//
//	/**
//	 * SimpleRealMatrix rank
//	 *
//	 * @return effective numerical rank, obtained from SVD.
//	 */
//	public int rank()
//	{
//		return new StewartSingularValueDecomposition(this).rank();
//	}
//
//	/**
//	 * SimpleRealMatrix condition (2 norm)
//	 *
//	 * @return ratio of largest to smallest singular value.
//	 */
//	public double cond()
//	{
//		return new StewartSingularValueDecomposition(this).norm2ConditionDouble();
//	}
//
//	/**
//	 * SimpleRealMatrix trace.
//	 *
//	 * @return sum of the diagonal elements.
//	 */
//	public double trace()
//	{
//		double t = 0;
//		for(int i = 0; i < Math.min(this.height, this.width); i++)
//			t += this.matrixElements[i][i];
//		return t;
//	}
//
//	/**
//	 * Generate matrix with RANDOM elements
//	 *
//	 * @param height Number of rows.
//	 * @param width Number of colums.
//	 * @return An height-by-width matrix with uniformly distributed RANDOM
//	 *         elements.
//	 */
//	public static SimpleRealMatrix random(final int height, final int width)
//	{
//		final SimpleRealMatrix A = new SimpleRealMatrix(height, width);
//		final double[][] X = A.matrixElements;
//		for(int i = 0; i < height; i++)
//			for(int j = 0; j < width; j++)
//				X[i][j] = Math.random();
//		return A;
//	}
//
//	/**
//	 * Generate identity matrix
//	 *
//	 * @param height Number of rows.
//	 * @param width Number of colums.
//	 * @return An height-by-width matrix with ones on the diagonal and zeros
//	 *         elsewhere.
//	 */
//	public static RealMatrix identity(final int height, final int width)
//	{
//		final double[][] identityValues = new double[height][width];
//		for(int index = 0; index < (height < width ? height : width); index++)
//			identityValues[index][index] = 1.0;
//		return new SimpleRealMatrix(identityValues);
//		/*
//		SimpleRealMatrix A = new SimpleRealMatrix(height, width);
//		double[][] fillMatrix = A.matrixElements;
//		for(int heightIndex = 0; heightIndex < height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < width; widthIndex++)
//				fillMatrix[heightIndex][widthIndex] = (heightIndex == widthIndex ? 1.0 : 0.0);
//		return A;*/
//	}
//
//	/**
//	 * Check if size(matrixElements) == size(operand) *
//	 */
//	private void checkMatrixDimensions(final RealMatrix compareMatrix)
//	{
//		if( compareMatrix.getHeight() != this.height || compareMatrix.getWidth() != this.width )
//			throw new IllegalArgumentException("Matrix dimensions must agree.");
//	}
//
//	@Override
//	public String toString()
//	{
//		final StringBuilder out = new StringBuilder("{");
//		for(int heightIndex = 0; heightIndex < this.height; heightIndex++)
//			for(int widthIndex = 0; widthIndex < this.width; widthIndex++)
//			{
//				if( widthIndex == 0 )
//					out.append('{');
//				out.append(this.matrixElements[heightIndex][widthIndex]);
//				if( widthIndex < (this.width - 1) )
//					out.append(',');
//				else
//					out.append('}');
//			}
//		out.append('}');
//		return out.toString();
//	}
}
