/**
 * Created by zxp on 2017/11/15.
 */
angular.module('app')
    .directive('echartsColumnInfo', function() {
        return {

            scope: {
                id: "@",
                legendData: "=",
                seriesData: "=",
                etitle:"@"
            },
            restrict: 'AE',
            template: '<div style="height:250px;"></div>',
            replace: true,
            link: function($scope, element, attrs, controller) {

                $scope.$watch('legendData + seriesData',function () {
                    var option = {

                        title: {
                            text: $scope.etitle,
                            subtext: '（万元）',
                            y: 'top',
                            x:'center'
                        },
                        tooltip : {
                            trigger: 'axis'
                        },
                        grid: {
                            left: '2%',
                            right: '2%',
                            bottom:'4%',
                            top:'4%',
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data : $scope.legendData,
                                //设置字体倾斜
                                axisLabel:{
                                    interval:0,
                                    rotate:45,//倾斜度 -90 至 90 默认为0
                                    margin:10
                                },
                                axisTick: {
                                    alignWithLabel: true
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                name:'所占比',
                                itemStyle: {

                                    //通常情况下：
                                    normal:{
                                        //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                        color: function (params){
                                            var colorList = ['#5dafe8','rgb(98,199,85)','rgb(245,192,80)','rgb(237,107,95)','#8497f6'];
                                            return colorList[params.dataIndex];
                                        },
                                    },
                                    //鼠标悬停时：
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                },
                                label: {
                                    normal: {
                                        show: true,
                                        formatter: '{c}',
                                        fontSize:12,
                                        color:"#000",
                                        rotate:45
                                    }
                                },
                                type:'bar',
                                barWidth: '40%',
                                data:$scope.seriesData

                            }
                        ]
                    };
                    var myChart = echarts.init(document.getElementById($scope.id),'macarons');
                    myChart.setOption(option,true);
                },true)
            }
        };
    });
