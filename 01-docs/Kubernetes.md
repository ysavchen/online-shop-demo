## Kubernetes

1. Build image with JDK 21
```bash
./gradlew bootBuildImage -p ./04-services/book-service
```

2. Test image
```bash
docker run --rm -it -p 8090:8090 ysavchen/book-service:1.0.0
```

3. Push image to Docker Hub
```bash
docker push ysavchen/book-service:1.0.0
```

4. Start Kubernetes with profile name 'local-cluster'
```bash
minikube start --driver=docker --container-runtime=containerd --nodes 3 -p local-cluster
```

5. Apply manifests
```bash
kubectl apply -f ./02-infra/kubernetes/manifests --recursive
```

6. Set namespace to online-shop-demo
```bash
kubectl config set-context --current --namespace=online-shop-demo
```

7. Set up Nginx Ingress
```
minikube addons enable ingress -p local-cluster
kubectl apply -f ingress.yaml
minikube tunnel -p local-cluster
```

8. Stop Kubernetes
```bash
minikube stop -p local-cluster
```