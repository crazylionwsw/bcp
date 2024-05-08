angular.module('app')
  .directive('deviceInfo', function(deviceService) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: '<span>{{device.serialNum}}(序号) - {{device.identify}}(识别码) - {{device.deviceType}}(类型) - {{device.deviceName}}(名称)</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                deviceService.getOne(scope.id, function(data){
                    scope.device = data;
                })
            }
        }
    };
  });