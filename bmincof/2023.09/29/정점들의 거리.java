import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

// author   : bmincof
// date     : 2023-09-29
public class Main {    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());

        int[][] parents = new int[16][N + 1];
        int[] distFromRoot = new int[N + 1];
        int[] depth = new int[N + 1];

        List<int[]>[] tree = new LinkedList[N + 1];
        for (int i = 1; i <= N; i++) {
            tree[i] = new LinkedList<>();
            distFromRoot[i] = -1;
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());

            tree[u].add(new int[]{v, dist});
            tree[v].add(new int[]{u, dist});
        }

        // dfs로 루트 부터의 거리 계산 + 부모 노드 찾기
        Deque<int[]> stack = new LinkedList<>();
        stack.push(new int[]{1, 0});
        distFromRoot[1] = 0;

        while (!stack.isEmpty()) {
            int[] curr = stack.pop();

            for (int[] next : tree[curr[0]]) {
                // 방문한 적 있는 노드이면
                if (distFromRoot[next[0]] != -1) {
                    continue;
                }
                distFromRoot[next[0]] = distFromRoot[curr[0]] + next[1];
                stack.push(new int[]{next[0], distFromRoot[next[0]]});
                depth[next[0]] = depth[curr[0]] + 1;
                parents[0][next[0]] = curr[0];
            }
        }

        // 부모 노드를 희소 테이블로 생성
        for (int i = 1; i < 16; i++) {
            for (int j = 1; j <= N; j++) {
                parents[i][j] = parents[i - 1][parents[i - 1][j]];
            }
        }

        // 두 노드 사이의 거리 찾기
        StringBuilder answer = new StringBuilder();
        int M = Integer.parseInt(br.readLine());
        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());

            int deep = Integer.parseInt(st.nextToken());
            int shallow = Integer.parseInt(st.nextToken());

            if (depth[deep] < depth[shallow]) {
                int tmp = deep;
                deep = shallow;
                shallow = tmp;
            }

            // 두 노드 사이의 거리 =
            //      deep의 루트에서 거리 + shallow의 루트에서 거리 - 2 * LCA의 루트에서 거리
            int depthDeep = distFromRoot[deep];
            int depthShallow = distFromRoot[shallow];

            // 두 노드가 같은 깊이가 될 때까지 더 아래에 있는 노드를 이동
            int diff = depth[deep] - depth[shallow];
            for (int i = 15; i >= 0; i--) {
                if ((diff & (1 << i)) != 0) {
                    deep = parents[i][deep];
                }
            }

            // LCA 찾기
            for (int i = 15; i >= 0; i--) {
                if (parents[i][deep] == parents[i][shallow]) {
                    continue;
                }
                deep = parents[i][deep];
                shallow = parents[i][shallow];
            }

            int lca = (deep == shallow) ? deep : parents[0][deep];
            int depthLca = distFromRoot[lca];
            answer.append(depthDeep + depthShallow - 2 * depthLca).append("\n");
        }

        System.out.print(answer);
    }
}