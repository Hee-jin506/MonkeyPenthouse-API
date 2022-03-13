if [ -d /home/ubuntu/build ]; then
    rm -rf /home/ubuntu/build
fi
# 만약 /home/ubuntu/build 디렉토리가 존재하면 지운다는 의미입니다.

mkdir -vp /home/ubuntu/build
# 다시 새로운 /home/ubuntu/build 디렉토리를 생성합니다.

docker stop api-server-spring
docker rm api-server-spring

if [[ "$(docker images -q monkeypenthouse/api-server-spring:v1 2> /dev/null)" != "" ]]; then
docker rmi -f $(docker images --format '{{.Repository}}:{{.Tag}}' --filter=reference='monkeypenthouse/api-server-spring:v1')
fi
# 해당 Docker Image가 존재하면 image를 제거한다는 뜻입니다.
# 이후 afterinstall.bash 파일에서 갱신된 이미지를 불러올 것입니다.
