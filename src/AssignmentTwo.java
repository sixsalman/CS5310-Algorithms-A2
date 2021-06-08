import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * This program sorts the unsorted numbers using four different versions of quick sort and times the processing. It
 * also gives sorted inputs to one of them to observe its worst case behavior
 * The four versions are :
 * 1 - pivotitem set at A[low]
 * 2 - pivotitem using median of three
 * 3 - randomized or probabilistic algorithm RQuickSort
 * 4 - randomized algorithm RSort with sample size n/(log n)^2
 * Versions 3 & 4 are mentioned in section 3.5.2 of Computer Algorithms book by Horowitz, Sahni and Rajasekaran
 *
 * @author M. Salman Khan
 */
class AssignmentTwo {

    public static void main(String[] args) {
        int[] numArray = new int[5000];
        int numOfTrials = 1000;
        long stTime, enTime;

        System.out.println("Run with data showing the worst case behavior (sorted data) of version 1 (pivotitem set " +
                "at A[low])");

        for (int n = 500; n <= 5000; n += 500) {
            System.out.printf("n = %d -> ", n);

            long totTime = 0;
            for (int i = 0; i < n; i++) numArray[i] = i + 1;

            for (int i = 0; i < numOfTrials; i++) {
                stTime = System.nanoTime();
                quickSort(numArray, 0, n - 1, 1, false);
                enTime = System.nanoTime();

                totTime += enTime - stTime;
            }

            System.out.printf("%d nanoseconds\n", totTime / numOfTrials);
        }

        System.out.println("\nRun with a random data set for all methods");

        Random randGen = new Random();
        int[] numArrayCopy = new int[5000];

        for (int n = 500; n <= 5000; n += 500) {
            System.out.println("n = " + n);

            long[] totTimes = {0, 0, 0, 0};

            for(int i = 0; i < numOfTrials; i++) {

                for (int j = 0; j < n; j++) numArrayCopy[j] = randGen.nextInt(n);

                for (int version = 1; version <= 4; version++) {
                    for (int j = 0; j < n; j++) numArray[j] = numArrayCopy[j];
                    stTime = System.nanoTime();
                    quickSort(numArray, 0 , n - 1, version, false);
                    enTime = System.nanoTime();
                    totTimes[version - 1] += enTime - stTime;
                }
            }

            for (int version = 1; version <= 4; version++) {
                System.out.printf("Version %d -> %d nanoseconds\n", version, totTimes[version - 1] / numOfTrials);
            }
            System.out.print("\n");
        }

        System.out.println("Run for a small array size, and print the pivot index and element, and the subarrays " +
                "from the Partition function for each recursive call of the quicksort algorithms\n" +
                "Version 1 - pivotitem set at A[low]");
        printPartitions(1);

        System.out.println("\nVersion 2 - pivotitem using median of three");
        printPartitions(2);

        System.out.println("\nVersion 3 - randomized or probabilistic algorithm RQuickSort");
        printPartitions(3);

        System.out.println("\nVersion 4 - randomized algorithm RSort");
        printPartitions(4);
    }

    /**
     * Sorts numbers '7,5,10,8,3,4,6,2,9,1' using the specified 'version' of quick sort and prints unsorted input,
     * pivot index, pivot element, subarrays and sorted output
     * @param version the corresponding number of the quick sort version to use
     */
    static void printPartitions(int version) {
        int[] numArray = {7,5,10,8,3,4,6,2,9,1};

        System.out.print("Unsorted Input: ");
        for (int i = 0; i < numArray.length; i++) System.out.print(numArray[i] + " ");
        System.out.print("\n");

        quickSort(numArray, 0 , numArray.length - 1, version, true);

        System.out.print("Sorted Output: ");
        for (int i = 0; i < numArray.length; i++) System.out.print(numArray[i] + " ");
        System.out.print("\n");
    }

    /**
     * Sorts 'numArray' using quick sort algorithm's specified 'version' starting from index 'low' (inclusive) uptil
     * index 'high' (inclusive)
     * @param numArray the array to sort
     * @param low index where sorting should start (inclusive)
     * @param high index where sorting should end (inclusive)
     * @param version the corresponding number of the quick sort version to use
     * @param print if true, pivot index, pivot element and subarrays are printed; if false, they are not printed
     */
    static void quickSort(int[] numArray, int low, int high, int version, boolean print) {
        if (low < high) {
            if(version == 2) { //pivotitem using median of three
                int[] firstMidLast = {numArray[low], numArray[(low + high) / 2], numArray[high]};
                Arrays.sort(firstMidLast);

                if(firstMidLast[1] == numArray[high]) {
                    swap(numArray, low, high);
                } else if(firstMidLast[1] == numArray[(low + high) / 2]) {
                    swap(numArray, low, (low + high) / 2);
                }
            } else if(version == 3 && high - low > 5) { //randomized or probabilistic algorithm RQuickSort
                Random randGen = new Random();
                int randIndex = randGen.nextInt(high + 1 - low) + low;

                swap(numArray, low, randIndex);
            } else if(version == 4) { //randomized algorithm RSort
                Random randGen = new Random();
                Integer[] sampleIndices = new Integer[(int)(numArray.length / Math.pow((Math.log(numArray.length) /
                        Math.log(2)), 2))];

                for(int i = 0; i < sampleIndices.length; i++) {
                    sampleIndices[i] = randGen.nextInt(numArray.length - i);

                    swap(numArray, sampleIndices[i], high - i);
                    sampleIndices[i] = high - i;
                }

                Arrays.sort(sampleIndices, Comparator.comparingInt(entry -> numArray[entry]));

                int lastPivotIndex = -1;

                for (int i = 0; i < sampleIndices.length; i++) {
                    swap(numArray, lastPivotIndex + 1, sampleIndices[i]);

                    int thisPivotIndex = partition(numArray, lastPivotIndex + 1, high - sampleIndices.length
                            + i + 1, print);
                    quickSort(numArray, lastPivotIndex + 1, thisPivotIndex, 1, print);

                    lastPivotIndex = thisPivotIndex;
                }

                if (lastPivotIndex != high) quickSort(numArray, lastPivotIndex + 1, high, 1, print);

                return;
            }

            int pivotIndex = partition(numArray, low, high, print);
            quickSort(numArray, low, pivotIndex - 1, version, print);
            quickSort(numArray, pivotIndex + 1, high, version, print);
        }
    }

    /**
     * Partitions 'numArray' as per quick sort requirements using its 'low' index as pivot
     * @param numArray the array to partition
     * @param low index where partitioning should start (inclusive)
     * @param high index where partitioning should end (inclusive)
     * @param print if true, pivot index, pivot element and subarrays are printed; if false, they are not printed
     * @return new index of pivot
     */
    static int partition(int[] numArray, int low, int high, boolean print) {
        int j = low;

        for (int i = low + 1; i <= high; i++) {
            if(numArray[i] < numArray[low]) {
                j++;
                swap(numArray, i, j);
            }
        }
        swap(numArray, low, j);

        if(print) {
            System.out.printf("Pivot Index: %d\tPivot Element: %d\tSubarray: ", j, numArray[j]);
            for (int i = low; i <= high; i++) System.out.print(numArray[i] + " ");
            System.out.print("\n");
        }

        return j;
    }

    /**
     * Swaps value at 'indexOne' with value at 'indexTwo' in 'numArray'
     * @param numArray the array in which to swap
     * @param indexOne index of value to be swapped with value at 'indexTwo'
     * @param indexTwo index of value to be swapped with value at 'indexOne'
     */
    static void swap(int[] numArray, int indexOne, int indexTwo) {
        int temp = numArray[indexOne];
        numArray[indexOne] = numArray[indexTwo];
        numArray[indexTwo] = temp;
    }

}