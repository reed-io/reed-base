/**
 * IdeaProject @ org.reed/Solution.java
 */
package org.reed;

import java.util.*;

/**
 * @author chenxiwen
 * @createTime 2019年04月12日 下午4:21
 * @description
 */
class Solution {
    public void gameOfLife(int[][] board) {
        int m = board.length;
        int n = m>0?board[0].length:0;
        int[][] result = new int[m][n];
//        System.out.println(m+", "+n);
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                int alive = 0;
                if(i-1>=0 && j-1>=0){
                    alive = board[i-1][j-1]==1?++alive:alive;
                }
                if(i-1>=0){
                    alive = board[i-1][j]==1?++alive:alive;
                }
                if(i-1>=0 && j+1<n){
                    alive = board[i-1][j+1]==1?++alive:alive;
                }
                if(j-1>=0){
                    alive = board[i][j-1]==1?++alive:alive;
                }
                if(j+1<n){
                    alive = board[i][j+1]==1?++alive:alive;
                }
                if(i+1<m && j-1>=0){
                    alive = board[i+1][j-1]==1?++alive:alive;
                }
                if(i+1<m){
                    alive = board[i+1][j]==1?++alive:alive;
                }
                if(i+1<m && j+1<n){
                    alive = board[i+1][j+1]==1?++alive:alive;
                }

                if(board[i][j] == 1){
                    if(alive<2){
                        result[i][j] = 0;
                    }else if(alive==2 || alive==3){
                        result[i][j] = 1;
                    }else if(alive>3){
                        result[i][j] = 0;
                    }
                }else{
                    if(alive==3){
                        result[i][j] = 1;
                    }
                }

            }
        }


        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                board[i][j] = result[i][j];
            }
        }

    }

    static void printArray(int[][] arr){
        int m = arr.length;
        int n = m>0?arr[0].length:0;
        System.out.println("[");
        for(int i=0;i<m;i++){
            System.out.print("\t[");
            for(int j=0;j<n;j++){
                System.out.print(arr[i][j]);
                if(j+1!=n){
                    System.out.print(",");
                }
            }
            System.out.print("]");
            if(i+1!=m){
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println("]");
    }


    public String removeDuplicateLetters(String s) {
        List<Character> cList = new ArrayList<Character>();
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(!cList.contains(c)){
                cList.add(c);
            }else{
                cList.remove(c);
                cList.add(c);
            }
        }
        StringBuffer strBuf = new StringBuffer();
        for(char c:cList){
            strBuf.append(c);
        }
        return strBuf.toString();
    }

    public int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i=0;i<nums.length;i++){
            if(map.containsKey(nums[i])){
                map.put(nums[i], map.get(nums[i])+1);
            }else{
                map.put(nums[i], 1);
            }
        }
        int result = 0;
        for(Integer i : map.keySet()){
            if(map.containsKey(result)){
                result = map.get(i)>map.get(result)?i:result;
            }else{
                result = i;
            }
        }
        return result;
    }


    public boolean searchMatrix_huishu(int[][] matrix, int target) {
        if(matrix.length == 0){
            return false;
        }
        int i=0,j=0;
        int temp = 0;
        while(true){
            if(matrix[i].length == 0){
                return false;
            }

            if(i<matrix.length && j==matrix[i].length){
                i=i+1==matrix.length?matrix.length-1:i+1;
                j=j-1<0?0:j-1;
            }

            System.out.println(i+","+j);

            if(target<matrix[i][0]){
                return false;
            }

            if(matrix[i][j] == target){
                return true;
            }
            if(i+1==matrix.length){
                if(j+1==matrix[i].length && matrix[i][j] < target){
                    return false;
                }
                if(matrix[i][j] < target && j+1<=matrix[i].length-1 && target < matrix[i][j+1]){
                    return false;
                }

                if(j==0 && matrix[i][j] > target){
                    return false;
                }
            }


            if(matrix[i][j]<target){
                temp = matrix[i][j];
                if(j<matrix[i].length){
                    j++;
                }else{
                    i=i+1==matrix.length?matrix.length-1:i+1;
                }
            }else{
                if(j-1>=0 && (matrix[i][j-1]==temp || matrix[i].length==1)){
                    i=i+1==matrix.length?matrix.length-1:i+1;
                }
                j=j-1<0?0:j-1;
            }

        }
    }

    public boolean searchMatrix1(int[][] matrix, int target) {
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;){
                System.out.println(i+", "+j);
                if(target == matrix[i][j]){
                    return true;
                }
                if(target>matrix[i][j]){
                    j++;
                }else{
                    break;
                }
            }
        }
        return false;
    }

    public boolean searchMatrix2(int[][] matrix, int target) {
        int temp = 0;
        for(int i=0, j=0;i<matrix.length;){
            System.out.println(i+", "+j);
            if(matrix[i].length==0 || matrix[i][0] > target){
                return false;
            }
            if(matrix[i][j] == target){
                return true;
            }
            temp = matrix[i][j];
            if(matrix[i][j] < target && j<matrix[i].length-1){
                j++;
            }
            else if(matrix[i][j] > target && j>0){
                j--;
                if(matrix[i][j] < target ){
                    i++;
                }
            }
            else{
                i++;
            }

        }
        return false;
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix.length==0 || matrix[0].length==0){
            return false;
        }
        int i=0,j=matrix[i].length-1;
        while(i<matrix.length){
            if(matrix[i][0]>target){
                return false;
            }
            if(matrix[i][j] == target){
                return true;
            }
            if(matrix[i][j]<target){
                i++;
            }else{
                j--;
            }
        }
        return false;
    }

    public List<Interval> merge(List<Interval> intervals) {
       List<Interval> list = new ArrayList<>();
       Collections.sort(intervals, new Comparator<Interval>() {
           @Override
           public int compare(Interval o1, Interval o2) {
               return o1.start>o2.start?1:-1;
           }
       });


       return list;
    }

    public static void main(String[] args){
//        int[][] board = {{0,1,0},{0,0,1},{1,1,1},{0,0,0}};
//        printArray(board);
        Solution solution = new Solution();
//        solution.gameOfLife(board);
//        printArray(board);

//        System.out.println(solution.removeDuplicateLetters("cbacdcbc"));
//        System.out.println(solution.majorityElement(new int[]{3,2,3}));

        int[][] matrix = {{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}};
//        int[][] matrix = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20},{21,22,23,24,25}};
//        int[][] matrix = {{-1,3}};
//        int[][] matrix = {{-5}};
//        int[][] matrix = {{1,1}};
//        int[][] matrix = {{5},{6}};
//        int[][] matrix = {{-1},{-1}};
//        int[][] matrix = {{1,4},{2,5}};
//        int[][] matrix = {{1,3,5,7,9},{2,4,6,8,10},{11,13,15,17,19},{12,14,16,18,20},{21,22,23,24,25}};
        long l1 = System.currentTimeMillis();
        System.out.println(solution.searchMatrix(matrix, 20));
        System.out.println(System.currentTimeMillis()-l1);
        long l = Long.MIN_VALUE;
        while(true){
            l++;
        }


    }

public class Interval {
      int start;
      int end;
      Interval() { start = 0; end = 0; }
      Interval(int s, int e) { start = s; end = e; }
}

}
