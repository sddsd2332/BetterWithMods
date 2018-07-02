#!/usr/bin/env bash
declare -a array=(white orange magenta light_blue yellow lime pink gray silver cyan purple blue brown green red black)
declare -a array2=(White Orange Magenta LightBlue Yellow Lime Pink Gray LightGray Cyan Purple Blue Brown Green Red Black)


template=`cat candle_.json`
i=0

for i in 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
do
    text="${template/color/${array[i]}}"
    text="${text/Color/${array2[i]}}"
    echo ${text} >> "candle_${array[i]}.json"
done
