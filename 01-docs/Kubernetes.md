## Kubernetes

1. Build image with JDK 21
```bash
cd 04-services/book-service
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
```bash
cd 02-infra/kubernetes/manifests
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