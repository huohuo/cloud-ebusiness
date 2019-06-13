#!/bin/sh

LOG_DIR=/var/logs/es1
ES_PATH_CONF=/conf/es1
ES_PATH=/espath

export ES_NETWORK_HOST=127.0.0.1
export ES_PATH_CONF

${ES_PATH}/elasticsearch/bin/elasticsearch -d -p ${LOG_DIR}/elasticsearch.pid
