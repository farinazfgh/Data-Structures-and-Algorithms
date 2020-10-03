public class BidirectionalDijkstra {
    /*
GR ← ReverseGraph(G)
Fill dist, distR with +∞ for each node
dist[s] ← 0, distR[t] ← 0
Fill prev, prevR with None for each node
proc ← empty, procR ← empty
do:
v ← ExtractMin(dist)
Process(v, G, dist, prev, proc)
if v in procR:
return ShortestPath(s, dist, prev, proc, t, . . . )
vR ← ExtractMin(distR)
repeat symmetrically for vR as for v
while True


***************************
Relax(u, v, dist, prev)
if dist[v] > dist[u] + w(u, v):
dist[v] ← dist[u] + w(u, v)
prev[v] ← u

***************************
Process(u, G, dist, prev, proc)
for (u, v) ∈ E(G):
Relax(u, v, dist, prev)
proc.Append(u)
****************************
ShortestPath(s, dist, prev, proc, t, distR, prevR, procR)
distance ← +∞, ubest ← None
for u in proc + procR:
if dist[u] + distR[u] < distance:
ubest ← u
distance ← dist[u] + distR[u]
path ← empty
last ← ubest
while last ̸= s:
path.Append(last)
last ← prev[last]
path ← Reverse(path)
last ← ubest
while last ̸= t:
last ← prevR[last]
path.Append(last)
return (distance, path)
     */
}
