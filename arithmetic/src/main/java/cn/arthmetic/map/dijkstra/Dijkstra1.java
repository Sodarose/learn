package cn.arthmetic.map.dijkstra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: kangkang
 * @Date: 2021/9/24 12:02
 * @Description: Dijkstra 最短路径算法
 */
public class Dijkstra1 {

    public static void main(String[] args) {
        int[][] map = new int[][]{
                {0, 5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {5, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 1},
                {3, Integer.MAX_VALUE, 0, 2, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 2, 0, 1},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0}
        };
        dijkstra(map, 0);
    }


    /**
     * 根据起点找到所有路径的最短路径
     *
     * @param map   图
     * @param start 七点
     */
    public static void dijkstra(int[][] map, int start) {
        // 目标点到各个点的最短距离
        int[] minRoad = new int[map[start].length];
        // 初始化所有的点都是未确定的点
        Arrays.fill(minRoad, -1);
        // 剩余的未计算的点集合
        Set<Integer> points = new HashSet<>();
        for (int i = 0; i < minRoad.length; i++) {
            points.add(i);
        }
        while (!points.isEmpty()) {
            int point = -1;
            int distance = Integer.MAX_VALUE;
            // 遍历所有的点
            for (int p : points) {
                // start到P点的距离
                int tempDistance = map[start][p];
                // 计算P点最短距离
                for (int i = 0; i < map[start].length; i++) {
                    // 未确定的点跳过
                    if (minRoad[i] == -1) {
                        continue;
                    }
                    // 与点不相连的跳过
                    if (map[i][p] == Integer.MAX_VALUE) {
                        continue;
                    }
                    // 确定最小距离
                    tempDistance = Math.min(minRoad[i] + map[i][p], tempDistance);
                }
                // 判断p点是否是当前的最小距离点
                if (tempDistance <= distance) {
                    point = p;
                    distance = tempDistance;
                }
            }
            // 确定当前轮次的
            minRoad[point] = distance;
            points.remove(point);
        }
        for (int i = 0; i < minRoad.length; i++) {
            System.err.println("point:" + i + "\t" + "distance:" + minRoad[i]);
        }
    }
}
