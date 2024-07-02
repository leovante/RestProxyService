package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

/**
 * Банкомат.
 * Инициализируется набором купюр и умеет выдавать купюры для заданной суммы, либо отвечать отказом.
 * При выдаче купюры списываются с баланса банкомата.
 * Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽. []
 */
class ATM {
    // место для кода
    private Map<Integer, Integer> bp;

    private ATM(Map<Integer, Integer> map) {
        if (map == null || map.isEmpty()) {
            throw new RuntimeException("Нет денег");
        }
        this.bp = map;
    }

    public Map<Integer, Integer> getMoney(int sum) {
        Integer returnSumDelta = sum;//0
        Map<Integer, Integer> returnSum = new HashMap<>();//0

        var bpSorted = bp.keySet().stream().sorted(Comparator.reverseOrder()).toList();//
        for (Integer blank : bpSorted) {
            if (returnSumDelta > blank && bp.containsKey(blank)) {
                var allSum = bp.get(blank) * blank;
                if (allSum >= returnSumDelta) {
                    int numBlank = returnSumDelta / blank;
                    returnSumDelta -= numBlank * blank;//0
                    returnSum.put(blank, numBlank);
                } else {
                    returnSumDelta -= allSum;
                    returnSum.put(blank, bp.get(blank));
                }
            }
        }

        if (returnSumDelta != 0) {
            throw new RuntimeException("Нет денег");
        }

        for (var numm : returnSum.entrySet()) {
            bp.put(numm.getKey(), bp.get(numm.getKey() - numm.getValue()));
        }

        return returnSum;
    }

}