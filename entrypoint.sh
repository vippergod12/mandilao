#!/bin/bash

# Start SQL Server in background
/opt/mssql/bin/sqlservr &

# Chờ SQL Server khởi động
echo "⏳ Waiting for SQL Server to start..."
sleep 20

# Tạo database nếu chưa tồn tại
echo "📦 Creating database WEB_NHAHANGLAU..."
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'Admin123!' -Q "IF DB_ID('WEB_NHAHANGLAU') IS NULL CREATE DATABASE WEB_NHAHANGLAU"

# Giữ container chạy
wait
