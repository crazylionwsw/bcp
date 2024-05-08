/**
 * Created by LB on 2016-10-24.
 */

app.factory('carTypeService',function (Restangular){
    return {

        getByIds: function (ids, callback){
            var rest = Restangular.all('/cartypes');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },

        deleteCarType:function (cartype,callback) {
            var id = cartype.id;
            var rest = Restangular.one('/cartype',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        saveCarType:function (cartype,callback){
            var rest = Restangular.all('/cartype');
            rest.post(cartype).then(function (data){
                callback(data);
            });
        },



        /**
         * 根据id获取
         * @param customer
         * @param callback
         */
        getCarType:function (id,callback){
            var rest = Restangular.one("/cartype",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /*
         * 根据ID查询品牌
         * */
        getCarBrand:function (id,callback) {
            var rest = Restangular.one("/carbrand",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 车辆品牌
         */
        getCarBrands: function (callback){
            var rest = Restangular.all('/carbrands');
            rest.get('').then(function(data){
                callback(data);
            });
        },
        
        /**
         * 车辆品牌
         */
        lookupCarBrands:function (callback) {
            var rest = Restangular.all('/carbrands/lookups/');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        /*
         * 根据ID查询车系
         * */
        getCarModel:function (id,callback) {
            var rest = Restangular.one("/carmodel",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        /*
         * 下级对象
         * */
        //查询列表数据
        getCarModels:function (id, callback){
            var rest = Restangular.one('/carbrand',id).all('/');
            return rest.all('/carmodels').getList("").then(function (data){
                callback(data);
            });
        },

        lookupCarModels:function (id, callback){
            var rest = Restangular.one('/carbrand',id).all('/');
            return rest.all('/carmodels/lookups').getList("").then(function (data){
                callback(data);
            });
        },
        
        //根据车系查车型
        getCarTypes:function (carmodel,currentPage,callback) {
            var rest = Restangular.one('/carmodel',carmodel.id).all('/');
            return rest.all('/cartypes').get('',{currentPage:currentPage}).then(function (data){
                callback(data);
            });
        },

        //根据车系查车型
        lookupCarTypes:function (id,callback) {
            var rest = Restangular.one('/carmodel',id).all('/');
            return rest.all('/cartypes/lookups').get('').then(function (data){
                callback(data);
            });
        },

        lookupCarTypesByCarModelIds:function (carModelIds,callback) {
            var rest = Restangular.all('/cartypes/lookups');
            return rest.post(carModelIds).then(function (data){
                callback(data);
            });
        },

        //根据品牌查车型
        getCarTypesByCarBrand:function (id,callback) {
            var rest = Restangular.one('/carbrand',id).all('cartypes');
            return rest.get('').then(function (data){
                callback(data);
            });
        },
        

        saveCarBrand:function (carbrand,callback){
            var rest = Restangular.all('/carbrand');
            rest.post(carbrand).then(function (data){
                callback(data);
            });
        },

        deleteCarBrand:function (carbrand,callback) {
            var id = carbrand.id;
            var rest = Restangular.one('/carbrand',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        
        checkUniqueCarBrand:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/carbrand/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        checkUniqueCarModel:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/carmodel/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        checkUniqueCarType:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/cartype/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        //      保存车系信息
        saveCarModel:function (carmodel,callback){
            var rest = Restangular.all('/carmodel');
            rest.post(carmodel).then(function (data){
                callback(data);
            });
        },

        deleteCarModel:function (carmodel,callback) {
            var id = carmodel.id;
            var rest = Restangular.one('/carmodel',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        syncCarBrand:function (id,callback) {
            var rest = Restangular.one("/carbrand",id).all('sync');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         *      通过 二手车 车架号获取    二手车评估信息
         * @param vin
         * @param callback
         */
        getOneByVin:function (vin, callback) {
            var rest = Restangular.one('/carvaluation',vin);
            rest.get('').then(function(data){
                callback(data);
            });
        }
    }
});
