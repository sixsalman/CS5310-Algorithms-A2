Assignment 2. Programming Assignment: Divide and Conquer.

- Implement: three versions of quicksort, (i) version with pivotitem set at A[low], (ii) version with pivotitem using median of three, (iii) randomized or probabilistic algorithm RQuickSort in slides or HSR book Section 3.5.2, and (iv) randomized algorithm RSort in HSR book Section 3.5.2. For (iv) set the number s of sample elements to s = n/(log n)^2.
  The Partition function for (i), (ii) and (iii) should differ only in how the pivotitem is set.

- Run with data showing the worst case behavior of (i), and with a random data set for all methods. Time and compare the performance of the algorithms.
  Also run for a small array size, and print the pivot index and element, and the subarrays from the Partition function for each recursive call of the quicksort algorithms.