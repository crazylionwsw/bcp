/**
 * Created by LB on 2016-10-24.
 */

app.factory('creditproductService',function (Restangular){
    return {
        getAll: function (callback){
            var creditproduct = Restangular.all('/creditproducts');
            creditproduct.get('').then(function(data){
                callback(data);
            });
        },
        getByIds: function (ids, callback){
            var creditproduct = Restangular.all('/creditproducts/');
            creditproduct.post(ids).then(function(data){
                callback(data);
            });
        },

        /**
         *删除
         */
        delete:function (creditproduct,callback) {
            var rest = Restangular.one('/creditproduct',creditproduct.id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        //还款方式列表
        getAllRepaymentWays:function (callback){
            var repaymentways = Restangular.all('/repaymentways/lookups');
            repaymentways.get('').then(function (data){
                callback(data);
            });
        },
        //业务类型列表
        getAllBusinessTypeIds:function (callback){
            var businessTypeIds = Restangular.all('/businesstypes/lookups');
            businessTypeIds.get('').then(function (data){
                callback(data);
            });
        },
        //担保方式列表
        getAllGuaranteeWays:function (callback){
            var guaranteeways = Restangular.all('/guaranteeways/lookups');
            guaranteeways.get('').then(function (data){
                callback(data);
            });
        },
        getOne:function(id,callback){
            var cardealer=Restangular.one("/creditproduct",id);
            return cardealer.get('').then(function(data){
                callback(data);
            });
        },
        //资金利率列表
        getAllSourceRates: function (callback){
            var sourcerates = Restangular.all('/sourcerates/lookups');
            sourcerates.get('').then(function(data){
                callback(data);
            });
        },
        //关于分页功能
        getPageData : function (currentPage,callback){
            var creditproduct = Restangular.all('/creditproducts?currentPage='+currentPage);
            return creditproduct.get('').then(function (data){
                callback(data);
            });
        },
        //保存分期产品信息
        save:function (creditproduct,callback){
            var creditproductMsg = Restangular.all('/creditproduct');
            creditproductMsg.post(creditproduct).then(function (data){
                callback(data);
            });
        },
        /**
         * 车辆品牌
         */
        getAllCarBrands: function (callback){
            var carbrands = Restangular.all('/carbrands/lookups');
            carbrands.get('').then(function(data){
                callback(data);
            });

        },
        //车型列表
        getAllCarTypesByBrand : function (carBrand,callback){
            var carbrand = Restangular.one("carbrand", carBrand.id);
            return carbrand.all("cartypes").getList().then(function (data){
                callback(data);
            });
        },
        //车辆用途
        getAllCreditUsages: function (callback){
            var creditUsagesInfo = Restangular.all('/creditusages');
            creditUsagesInfo.get('').then(function(data){
                callback(data);
            });
        },
        //查询省列表数据
        getAllRegions:function (callback){
            var provinceInfo = Restangular.all('/provinces/lookups');
            provinceInfo.get('').then(function (data){
                callback(data);
            });
        },
        checkUniqueCreditProduct:function(entity,propname,propval,callback){
            var creditproduct = Restangular.all('/validate/creditproduct/'+propname+'/'+propval);
            creditproduct.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});
