/**
 * Created by zxp on 2017/11/15.
 */
angular.module('app')
    .directive('echartsInfo', function() {
        return {
            scope: {
                id: '@',
                legendData: '=',
                seriesData: '=',
                etitle:'@'
            },
            restrict: 'AE',
            template: '<div style="height:200px; width: 280px; float: left"></div>',
            replace: true,
            controller: function($scope, $element, $attrs) {
                $scope.$watch('legendData + seriesData', function() {
                    var option = {
                        title: {
                            text: $scope.etitle,
                            x: 'center',
                            y: 'top'
                        },
                        label : {
                            normal: {
                                formatter: '{b}: \n {c} \n',
                                textStyle: {
                                    fontWeight: 'normal',
                                    fontSize: 12
                                }
                            }
                        },
                        legend: {
                            type:'plain',
                            orient: 'vertical',
                            data: $scope.legendData,
                            selected: $scope.legendData.selected
                        },
                        grid: {
                            left: '1%',
                            right: '1%',
                            bottom:'1%',
                            top:'4%',
                            containLabel: true
                        },
                        series: [
                            {
                                name: '所占比',
                                type: 'pie',
                                radius: ['40%', '55%'],
                                avoidLabelOverlap: true,//是否启用防止标签重叠策略，默认开启，在标签拥挤重叠的情况下会挪动各个标签的位置，防止标签间的重叠
                                data: $scope.seriesData,
                                label : {
                                    normal: {
                                        show: true,
                                        position: 'outside'
                                    },
                                    emphasis : {
                                        show : true,
                                        formatter: '{b}: \n {c} \n',
                                        textStyle : {
                                            fontSize : 15,
                                            fontWeight : 'bold'
                                        }
                                    }
                                },
                                labelLine:{
                                    normal:{
                                        show:true,
                                        length:10,
                                        length2:10,
                                        smooth:true
                                    }
                                },
                            }
                        ],
                        color: ['#5dafe8', 'rgb(98,199,85)', 'rgb(245,192,80)', 'rgb(237,107,95)']
                    };
                    var myChart = echarts.init(document.getElementById($scope.id));
                    myChart.setOption(option,true);

                },true)
            }
        };
    });
