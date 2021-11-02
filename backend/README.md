# online-shop-backend

Demo application for experiments

# Setting up development environment
Run Postgres in Docker
```
docker run --rm -d \
    --name online_shop_db \
    -e POSTGRES_DB=app_db \
    -e POSTGRES_USER=app_user \
    -e POSTGRES_PASSWORD=app_password \
    -v online_shop_data:/var/lib/postgresql/data \
    -p 5432:5432 \
    postgres:12
```