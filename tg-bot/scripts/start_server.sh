#!/usr/bin/env bash
cd /opt/projectx/
aws s3 cp s3://fsdjfgasfgjknfgdsjkn-sorce/config/bot.properties bot.properties
nohup java -Dlog4j.configuration=file:/opt/projectx/log4j.properties -jar ./tg-bot-1.0-SNAPSHOT.jar bot.properties > /dev/null 2>&1&