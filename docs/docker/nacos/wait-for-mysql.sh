#!/bin/bash

# 设置最大等待时间（秒）
MAX_WAIT=120
WAIT_INTERVAL=5

echo "检查 MySQL 服务是否就绪..."

# 检查 MySQL 端口
check_mysql() {
  mysql -h mysql -u root -p775825 -e "SELECT 1;" > /dev/null 2>&1
  return $?
}

count=0
while ! check_mysql; do
  count=$((count + WAIT_INTERVAL))

  if [ $count -ge $MAX_WAIT ]; then
    echo "错误：MySQL 在 $MAX_WAIT 秒内未准备就绪"
    exit 1
  fi

  echo "等待 MySQL 准备就绪... ($count/$MAX_WAIT 秒)"
  sleep $WAIT_INTERVAL
done

echo "MySQL 服务已就绪"