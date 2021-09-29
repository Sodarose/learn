package org.arthmetic.dijkstra;

import java.util.*;

/**
 * @Auther: kangkang
 * @Date: 2021/9/25 14:23
 * @Description: Dijkstra 邻接表最短路径算法
 */
public class Dijkstra2 {

    private static Map<Integer, Vertex> graph = new HashMap<>(2);

    public static void main(String[] args) {

    }

    /**
     * 执行最短路径算法
     *
     * @param startVertex 起点
     */
    public static void dijkstra(int startVertex) {
        Vertex start = graph.get(startVertex);
        if (start == null) {
            return;
        }
        // 待检查的队列
        TreeSet<Vertex> vertices = new TreeSet<>(Comparator.comparing(vertex -> vertex.dist));
        // 初始化一些参数
        for (Vertex vertex : graph.values()) {
            start.dist = vertex == start ? 0 : Integer.MAX_VALUE;
            start.prev = vertex == start ? start : null;
            vertices.add(vertex);
        }
        // 临时变量
        Vertex u, v;
        while (!vertices.isEmpty()) {
            // 取出当前距离start最近的点
            u = vertices.pollFirst();
            if (u == null) {
                break;
            }
            // 剩下的节点与start都并不相连，没有再计算下去的必要
            if (u.dist == Integer.MAX_VALUE) {
                break;
            }
            // 更新相连节点与start的距离
            for (Map.Entry<Vertex, Edge> entry : u.connectedVertexMap.entrySet()) {
                v = entry.getKey();
                // 相连节点到start的距离=当前点距离+当前点到相连节点的距离
                int newDist = u.dist + entry.getValue().dist;
                // 判断是否是最短距离
                if (newDist < v.dist) {
                    vertices.remove(v);
                    v.prev = u;
                    v.dist = newDist;
                    vertices.add(v);
                }
            }
        }
    }

    /**
     * @return Map<Integer, List < Edge>> 目标点，到目标的路径信息
     */
    private Map<Integer, List<Edge>> collect(Vertex start) {
        // 遍历图节点 收集路径信息 目标节点，路径信息
        Map<Integer, List<Edge>> vertexEdgeListMap = new HashMap<>();
        for (Vertex vertex : graph.values()) {
            // 无通路
            if (vertex.dist == Integer.MAX_VALUE) {
                continue;
            }
            // 自身节点
            if (vertex.dist == 0 || vertex.prev == vertex) {
                continue;
            }
            List<Edge> edges = new ArrayList<>();
            vertex.collect(edges);
            vertexEdgeListMap.put(vertex.key, edges);
        }
        return vertexEdgeListMap;
    }

    /**
     * 节点
     */
    private static class Vertex {
        /**
         * 唯一key
         */
        private int key;

        /**
         * 边
         */
        private Edge edge;

        /**
         * 目标点到该点的距离
         */
        private int dist;

        /**
         * 前一个节点
         */
        private Vertex prev;

        /**
         * 与之相连的节点
         */
        private Map<Vertex, Edge> connectedVertexMap;

        public Vertex(int key, Edge edge) {
            this.key = key;
            this.edge = edge;
            this.dist = Integer.MAX_VALUE;
            this.prev = this;
            this.connectedVertexMap = new HashMap<>(2);
        }

        /**
         * 递归遍历链表
         *
         * @param edges 需要经过的边
         */
        public void collect(List<Edge> edges) {
            if (prev != this) {
                prev.collect(edges);
            }
            edges.add(prev.edge);
        }

        // get and set

        public int getDist() {
            return dist;
        }

        public int getKey() {
            return key;
        }

        public Map<Vertex, Edge> getConnectedVertexMap() {
            return connectedVertexMap;
        }

        public Vertex getPrev() {
            return prev;
        }
    }


    /**
     * 边
     */
    private static class Edge {
        /**
         * 为唯一Id
         */
        private int id;

        /**
         * 相连节点(v1->v2)非双向情况下
         */
        private int v1, v2;

        /**
         * 距离
         */
        private int dist;

        /**
         * 是否为无向边
         */
        private boolean unoriented;


        public static Edge valueOf(int id, int v1, int v2, int dist, boolean unoriented) {
            Edge edge = new Edge();
            edge.id = id;
            edge.v1 = v1;
            edge.v2 = v2;
            edge.dist = dist;
            edge.unoriented = unoriented;
            return edge;
        }

        // get and set

        public int getDist() {
            return dist;
        }

        public int getId() {
            return id;
        }

        public int getV1() {
            return v1;
        }

        public int getV2() {
            return v2;
        }

        public boolean isUnoriented() {
            return unoriented;
        }
    }

}
