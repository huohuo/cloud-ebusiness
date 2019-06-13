#!/usr/bin/env bash
nohup /kibana/bin/kibana > /var/logs/kibana.nohup.out 2>&1 &
