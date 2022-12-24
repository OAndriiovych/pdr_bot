#!/usr/bin/env bash
cd /opt/prod/
aws s3 cp s3://tertertert-prod-sorce/config/bot.properties bot.properties
nohup java -Dlog4j.configuration=file:/opt/projectx/log4j.properties -jar ./tg-bot-1.0-SNAPSHOT.jar bot.properties > /dev/null 2>&1&