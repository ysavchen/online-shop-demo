# online-shop-demo

Demo application for experiments

### Run application locally
1. Start local infra
```bash
docker-compose up -d
```
2. Run services
```bash
./gradlew bootRun
```

### Kubernetes
1. Build native image with GraalVM for JDK 21
```
./gradlew bootBuildImage
```
2. Push image to Docker Hub
```bash
docker push ysavchen/book-service:0.0.1-SNAPSHOT
```
3. Start Kubernetes with profile name 'local-cluster'
```bash
minikube start --driver=docker --container-runtime=containerd --nodes 3 -p local-cluster
```
4. Apply manifests
```
kubectl apply -f deployment.yaml
```
5. Set namespace to online-shop-demo
```bash
kubectl config set-context --current --namespace=online-shop-demo
```
6. Stop Kubernetes
```bash
minikube stop -p local-cluster
```

### Observability
1. Check Prometheus reads metrics
```
http://localhost:9090/targets?search=
```
2. Check Grafana dashboard
```
username: admin
password: admin
```
