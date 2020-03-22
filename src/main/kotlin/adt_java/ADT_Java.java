package adt_java;

import java.util.function.Function;
import java.util.function.Supplier;

public class ADT_Java {


    public static void main(String[] args) {


        System.out.println("Hello World!");

    }



// For the sake of implicitly:
// * everything is immutable
// * all functions and methods are pure
// * null isn't a thing (for the most part)
// * performance doesn't really matter



// isomorphic:  (Integer, Boolean) ---> (Boolean, Integer)
//              (Integer, (String, Boolean)) ---> ((Integer, String), Boolean)




    @FunctionalInterface
    public interface FunCtion<T, R> extends Function<T, R> {

        static <T> FunCtion<T, T> identity() {
            return t -> t;
        }

        static <T, R> FunCtion<T, R> constant(R r) {
            return t -> r;
        }

        static <T, R> FunCtion<T, R> ofSupplier(Supplier<R> s) {
            return t -> s.get();
        }
    }


    @FunctionalInterface
    public interface BiFunCtion<T, U, R> {
        R apply(T t, U u);

        default BiFunCtion<U, T, R> flip() {
            return (u, t) -> this.apply(t, u);
        }
    }



    // Note: currying
    // f: T x U -> R
    // f: T -> (U -> R)
//    @FunctionalInterface
//    public interface CurriedBiFunCtion<T, U, R> extends FunCtion<T, FunCtion<U, R>> {
//
//        default FunCtion<U, R> apply(T t) {
//            return u -> this.apply(t);
//
//        }
//
//        @Override
//        default FunCtion<U, R> apply(T t) {
//            return u -> t
//        }
//
//
//        @Override
//        default FunCtion<U, R> apply(T t) {
//            return t -> u -> r;
//        }
//    }

}


