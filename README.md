# online-shop-demo

Demo application for experiments

### Run application locally
1. Start local infra
```bash
cd 02-infra
docker-compose up -d
```
2. Run services
```
./gradlew bootRun
```

### Kubernetes
1. Build native image with GraalVM for JDK 21
```bash
cd book-service
./gradlew bootBuildImage
```
2. Push image to Docker Hub
```bash
docker push ysavchen/book-service:1.0.0
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
6. Set up Nginx Ingress
```
minikube addons enable ingress -p local-cluster
kubectl apply -f ingress.yaml
minikube tunnel -p local-cluster
```
7. Stop Kubernetes
```bash
minikube stop -p local-cluster
```

### Observability

#### Logging
Service -> Loki -> Grafana<br/>
Service pushes structured logs to http://localhost:3100/loki/api/v1/push

#### Tracing
Service -> Tempo -> Grafana<br/>
Service pushes spans to http://localhost:9411/api/v2/spans

#### Metrics
Service -> Prometheus -> Grafana<br/>
Prometheus scrapes metrics from `<service url>/actuator/prometheus`<br/>
<img alt="observability" src="https://github.com/ysavchen/online-shop-demo/blob/main/01-schema/observability.png"/>

### Infrastructure
- Kafka UI: http://localhost:9095
- Prometheus: http://localhost:9090
- Alertmanager: http://localhost:9093
- Maildev: http://localhost:9094
- Grafana: http://localhost:3000

### Swagger
- book-service: http://localhost:8090/swagger-ui/index.html
- order-service: http://localhost:8091/swagger-ui/index.html