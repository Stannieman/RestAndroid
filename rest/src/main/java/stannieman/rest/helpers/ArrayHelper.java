package stannieman.rest.helpers;

/**
 * Class containing helper methods for arrays.
 */
public abstract class ArrayHelper {

    /**
     * Checks whether an array of a given type contains an object that's equal to the given object.
     * Comparison of objects is done using the equels(T object) method.
     * @param array array with objects
     * @param object object to look for in the array
     * @param <T> type of the object and objects in the array
     * @return whether the array contains an object equam to the given object
     */
    public static <T> boolean Contains(T[] array, T object){
        for (T arrayItem : array) {
            if (arrayItem.equals(object)) {
                return true;
            }
        }

        return false;
    }
}
