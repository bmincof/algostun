import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

// author   : bmincof
// date     : 2023-09-28
public class Main {
    static int id = 0;

    static int[] parent;
    static boolean[] selected;
    static List<Integer>[] graph;
    static Deque<Integer> passed = new ArrayDeque<>();
    static PriorityQueue<PriorityQueue<Integer>> scc = new PriorityQueue<>(Comparator.comparingInt(pq -> pq.peek()));

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // 정점의 개수, 간선의 개수
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());

        parent = new int[V + 1];
        selected = new boolean[V + 1];
        graph = new LinkedList[V + 1];
        for (int i = 1; i <= V; i++) {
            graph[i] = new LinkedList<>();
        }

        while (E-- > 0) {
            st = new StringTokenizer(br.readLine());
            graph[Integer.parseInt(st.nextToken())].add(Integer.parseInt(st.nextToken()));
        }

        // SCC 찾기
        for (int i = 1; i <= V; i++) {
            if (parent[i] == 0) {
                dfs(i);
            }
        }

        StringBuilder answer = new StringBuilder();

        answer.append(scc.size()).append("\n");
        while (!scc.isEmpty()){
            PriorityQueue<Integer> component = scc.poll();
            while (!component.isEmpty()){
                answer.append(component.poll()).append(" ");
            }
            answer.append("-1\n");
        }

        System.out.println(answer);
    }

    static int dfs(int curr) {
        // 노드 처음 방문 시 스택에 추가
        passed.push(curr);
        // 처음 부모노드는 본인으로
        int savedParent = parent[curr] = ++id;

        // 연결된 노드에서 탐색하기
        for (int next : graph[curr]) {
            // 방문한 적 있고 연결 요소가 확정되지 않았다면
            // 더 먼저 방문한 점을 부모 노드로 선택
            if (parent[next] != 0 && !selected[next]) {
                parent[curr] = Math.min(parent[curr], parent[next]);
            } else if (parent[next] == 0) {
                parent[curr] = Math.min(parent[curr], dfs(next));
            }
        }

        // 모두 순회하고 자기 자신으로 돌아왔을 때 부모노드가 자기 자신이라면
        // 스택에서 자기 자신을 만날 때까지 뽑기
        if (parent[curr] == savedParent) {
            PriorityQueue<Integer> component = new PriorityQueue<>();
            while (!passed.isEmpty()) {
                int node = passed.pop();
                selected[node] = true;
                component.offer(node);

                if (node == curr) {
                    break;
                }
            }
            scc.add(component);
        }

        return parent[curr];
    }
}