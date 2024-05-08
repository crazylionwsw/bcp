/**
 * Created by LB on 2016-10-24.
 */

app.factory('customerimageService',function (Restangular, $filter){
    return {

        //添加
        save:function (customerimage,callback){
            if(!customerimage.id)
                customerimage.dataStatus = 1;
            var customerimageInfo = Restangular.all('/customerimage');
            customerimageInfo.post(customerimage).then(function (data){
                callback(data);
            });
        },

        //查询客户合同
        getMergedCustomerContract:function (customerimage,callback) {
            var customerimageInfo = Restangular.all('/customercontract/merged');
            customerimageInfo.post(customerimage).then(function (data){
                callback(data);
            });
        },

        //合成文档
        mergeCustomerImages:function (force,customerimage,loginUserId,callback) {
            var customerimageInfo = Restangular.all('/customerimages/merge?force='+force+'&loginUserId='+loginUserId);
            customerimageInfo.post(customerimage).then(function (data){
                callback(data);
            });
        },
        
        //审核
        sign: function(customerimage, signinfo, callback) {
            //修改CustomerImage的有效状态
            if (signinfo.result != 2) {
                customerimage.isValid = false;
            }

            //为CustomerImage添加审批记录
            if (!customerimage.signInfos) customerimage.signInfos = [];
            customerimage.signInfos.push(signinfo);
            var customerimageInfo = Restangular.all('/customerimage');
            customerimageInfo.post(customerimage).then(function (data){
                callback(data);
            });
        },

        //删除
        delete:function(id,callback){
            var rest = Restangular.one('/customerimage', id);
            rest.remove().then(function (data){
                callback(data);
            });
        },

        //通过
        pass:function(customerimage,callback){
            customerimage.approveStatus = 9;
            var customerimageInfo = Restangular.all('/customerimage');
            customerimageInfo.post(customerimage).then(function(data){
                callback(data);
            })
        },

        //拒绝
        reject:function(customerimage,cardemand){
            customerimage.approveStatus = 12;
            var customerimageInfo = Restangular.all('/customerimage');
            customerimageInfo.post(customerimage).then(function(data){
                callback(data);
            })
        },

        getAllCustomerImageTypes:function (callback) {
            var imagetypes = Restangular.all('/imagetypes');
            imagetypes.get('').then(function (data) {
                callback(data);
            })
        },

        getByIds:function(ids,callback){
            var customerimageInfo = Restangular.all('/customerimages/');
            customerimageInfo.post(ids).then(function(data){
                callback(data);
            })
        },

        getTypesByIds:function(ids,callback){
            var customerimageInfo = Restangular.all('/imagetypes/');
            customerimageInfo.post(ids).then(function(data){
                callback(data);
            })
        },
        getTypesByCodes:function (codes, callback) {
            var customerimageInfo = Restangular.all('/imagetypes/bycodes/');
            customerimageInfo.post(codes).then(function(data){
                callback(data);
            })
        },
        
        getOne:function (id,callback){
            var rest = Restangular.one("/customerimage",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getOneByCode:function (code,callback) {
            var rest = Restangular.one("/imagetype/bycode",code);
            return rest.get('').then(function (data) {
                callback(data);
            });
        },

        createSavePdf:function(creditphotograph,callback){
            var rest = Restangular.all('/creditphotograph/pdf');
            rest.post(creditphotograph).then(function (data){
                callback(data);
            });
        },

        //  获取某交易的某几种客户档案
        getByCustomerIdAndCustomerTransactionIdAndImageTypeCodes:function(customerId,customerTransactionId,imageTypeCodeList,callback){
            var customerimageInfo = Restangular.all('/customerimages/'+customerId+'/'+customerTransactionId);
            customerimageInfo.post(imageTypeCodeList).then(function(data){
                callback(data);
            })
        },
        
        getImages:function (id,imageTypeCodeList,callback) {
            var rest = Restangular.all('/images/'+id);
            rest.post(imageTypeCodeList).then(function(data){
                callback(data);
            })
        },

        getParamImageCodesByBillTypeCodeAndBusinessTypeCode:function (businessTypeCode,billTypeCode, callback) {
            var rest = Restangular.one("/imageTypes/param/"+businessTypeCode+'/'+billTypeCode);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getDeclarationImageTypes:function (callback) {
            var rest  =Restangular.one("/imageTypes/param/declaration");
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
    }
});