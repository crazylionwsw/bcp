/**
 * Created by LB on 2016-10-18.
 */

'use strict';

app.controller('employeeController',['$scope','employeeService','$modal',function($scope,employeeService,$modal){
    $scope.title = '客户列表';
    /*列表*/
    $scope.employees  = {};//返回的列表数据

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        employeeService.getAll(function(data){

            $scope.employees = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1;//当前页

        });
    }

    /*分页功能*/
    $scope.employees = {};
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        employeeService.getPageData($scope.currentPage,function(data){
            $scope.employees = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1 ;//当前页
        });
    }

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (employee){

        /*模态框*/
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"employeeinsert.html",
            controller : function ($scope,$modalInstance){
                /*关闭模态框*/
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

                if(employee.id && employee.id != null){
                    /*根据id获取用户的信息*/
                    var employeeEdit = employee;
                    $scope.employee = employeeEdit;
                }else{
                    $scope.employee = {};
                    $scope.employee.cells =[];
                }
                var i = 0;
                $scope.num = [];
                $scope.addCell = function (){
                    $scope.num.push(i++);
                }
                // }
                /*保存用户信息*/
                $scope.save = function (){

                    var employee = $scope.employee;

                    employeeService.saveEmployee(employee,function (data){

                        window.location.reload(true);

                    });
                    modalInstance.close();
                    location.reload(true);
                }
            }
        });
    }
    /**
     * 删除用户
     */
    $scope.delete = function (employee,index){
        employeeService.remove(employee,function (data){
            $scope.changePage();
        })
    };
    

    /**
     * 模糊查询
     */
    $scope.searchEmployee = function () {
        employeeService.search($scope.search, function (data) {

            $scope.employees = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1;//当前页

        })

    }


}]);


/**
 * 日期组件
 */
app.controller('DatepickerPopupDemoCtrl', function ($scope) {
    //显示当前默认的时间
    $scope.today = function() {
        $scope.dt = new Date();
    };
    $scope.today();

    //选中固定日期
    $scope.clear = function() {
        $scope.dt = null;
    };

    $scope.inlineOptions = {
        customClass: getDayClass,
        minDate: new Date(),
        showWeeks: true
    };

    $scope.dateOptions = {
        dateDisabled: disabled,
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(),
        startingDay: 1
    };

    // Disable weekend selection
    function disabled(data) {
        var date = data.date,
            mode = data.mode;
        return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
    }

    $scope.toggleMin = function() {
        $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
        $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
    };

    $scope.toggleMin();

    $scope.open2 = function() {

        $scope.popup2.opened = true;
    };

    $scope.setDate = function(year, month, day) {
        $scope.dt = new Date(year, month, day);
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];

    $scope.popup2 = {
        opened: false
    };

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 1);
    $scope.events = [
        {
            date: tomorrow,
            status: 'full'
        },
        {
            date: afterTomorrow,
            status: 'partially'
        }
    ];

    function getDayClass(data) {
        var date = data.date,
            mode = data.mode;
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0,0,0,0);

            for (var i = 0; i < $scope.events.length; i++) {
                var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }

        return '';
    }
});