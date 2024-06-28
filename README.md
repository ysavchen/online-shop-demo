## online-shop-demo

Demo application for experiments

### Run application locally
1. Start local infra
```bash
docker-compose up -d
```
2. Check Prometheus reads metrics<br/>
   `http://localhost:9090/targets?search=`
3. Check Grafana dashboard<br/>
   username: admin<br/>
   password: admin

### Kubernetes
1. Build image<br/>
```bash
./gradlew bootBuildImage
```
2. Run service in Docker
```bash
docker run --rm -p 8090:8090 docker.io/library/book-service:0.0.1-SNAPSHOT
```
3. Start Kubernetes with profile name 'local-cluster'
```bash
minikube start --driver=docker --container-runtime=containerd --nodes 3 -p local-cluster
```
