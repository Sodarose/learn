package org.arthmetic.dijkstra;

import java.util.*;

/**
 * @Auther: kangkang
 * @Date: 2021/9/29 16:27
 * @Description: Dijkstra 邻接表最短路径算法2
 */
public class Dijkstra3 {

    private static Map<Integer, Vertex> graph = new HashMap<>(2);

    public static void main(String[] args) {

    }

    public static void dijkstra() {
        // 未确定的点
        List<Vertex> uncertainVertices = new ArrayList<>();
        // 已经确定的点
        List<Vertex> certainVertices = new ArrayList<>();
        while (!uncertainVertices.isEmpty()) {
            // 当前距离起始点最近的节点
            Vertex vertex;
            // 遍历所有的节点，找到当前距离起始点最近的点
            for (Vertex u : uncertainVertices) {
                // 从已经确定的节点找到与其连接的节点，并计算这个节点的最小值
                for (Vertex v : uncertainVertices) {
                    if (!v.connectedVertexMap.containsKey(u.key)) {
                        continue;
                    }
                }
            }

        }
    }

    private static class Vertex {
        private String key;
        private int dist;
        private Edge edge;
        private Map<Vertex, Edge> connectedVertexMap;
    }

    private static class Edge {
        private int v1;
        private int v2;
    }
}
