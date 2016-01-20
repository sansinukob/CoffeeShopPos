/**
 * Created by Laurie on 12/1/2015.
 */
orderApp.controller("orderController", function ($scope, $http) {
    $scope.loadSideNav = function(url){
        $http.get(url).then(function successCallback(response) {
            $scope.sideNav = response.data;
            getAddOn(response.data);
            $scope.loadProduct('/order/getAllProduct');
        }, function errorCallback(response) {

        });
    };

    $scope.loadBases = function(){
        $http.get("/item/load-base").then(function successCallback(response){
            $scope.bases = response.data;
        }, function errorCallback(response){

        });
    };

    $scope.clickSize = function(ingredients){
        if(isShowBases(ingredients)){
            for(var i=0;i<=ingredients.length-1;i++){
                if(ingredients[i].item.isBase !=null
                    && ingredients[i].item.isBase){
                    for(var j=0; j<=$scope.bases.length-1;j++){
                        if(ingredients[i].item.id == $scope.bases[j].id){
                            $("#base-"+$scope.bases[j].id).click();
                            break;
                        }
                    }
                    break;
                }
            }
        }
    };

    $scope.cacheUser = function(){
        $http.get("/login/get-logged").then(function successCallback(response){
            $scope.loggedUser = response.data.user;
            $scope.branch = response.data.branch;
        }, function errorCallback(response){

        });
    };

    $scope.setDateTime = function(datetime){
        $scope.dateTime = datetime;
    };

    var isShowBases = function(ingredient){
        var show = false;
        for(var i=0; i<=ingredient.length-1;i++){
            if(ingredient[i].item.isBase !=null
                && ingredient[i].item.isBase) {
                show = true;
                break;
            }
        }
        $scope.showBases = show;
        return show;
    };

    var getAddOn = function(data){
        for(var i=0;i<= data.length-1; i++){
            if(data[i] != null &&
            data[i].productGroupName == "Add-Ons"){
                $scope.addOnGroup = data[i];
                break;
            }
        }
    };

    var displayFirstGroup = function() {
        if($scope.sideNav != null &&
            $scope.sideNav != undefined){
            $scope.changeGroup($scope.sideNav[0].id);
        }
    };

    var getBase = function(data) {
        if(data!=null
            && data!=undefined){
            for(var i=0; i<=data.length-1; i++){
                if(data[i].item.isBase != null
                    && data[i].item.isBase){
                    for(var j = 0; j<= $scope.bases.length-1;j++){
                        if($scope.bases[j].id==data[i].item.id){
                            $scope.bases[j].isSelectedAsBase = true;
                            break;
                        }
                    }
                    break;
                }
            }
        }
    };

    $scope.loadProduct = function(url){
        $http.get(url).then(function successCallback(response) {
            $scope.product = response.data.results;
            $scope.loadAddOn(response.data.results);
            $scope.allProduct = response.data.all;
            displayFirstGroup();
        }, function errorCallback(response) {

        });
    };

    $scope.retrieveProduct = function(id){
        for(var i=0; i<=$scope.allProduct.length-1;i++){
            if($scope.allProduct[i].id == id){
                $scope.addToCart = $scope.allProduct[i];
                break;
            }
        }
    };

    $scope.loadAddOn = function(data){
        $scope.productAddOn = [];
        for(var i=0; i<=data.length-1;i++){
            if(data[i].productGroupList !=null){
                for(var j=0;j<=data[i].productGroupList.length-1;j++){
                    if($scope.addOnGroup.id == data[i].productGroupList[j].id){
                        $scope.productAddOn.push(data[i]);
                        break;
                    }
                }
            }
        }
    };

    $scope.changeGroup = function(id){
        $scope.productFound = [];
        angular.forEach($scope.product, function (value, key) {
            angular.forEach(value.productGroupList, function(group, groupKey){
                if(group.id == id){
                    $scope.productFound.push(value);
                }
            });
        });
    };

    $scope.selectProduct = function(id){
        $scope.selectedProduct = {};
        angular.forEach($scope.product, function (value, key) {
            if(value.id == id){
                $scope.selectedProduct = value;
                if(value.productUnder != null &&
                    value.productUnder.length >0){
                    $scope.sizeSelection = true;
                } else {
                    $scope.sizeSelection = false;
                }
                if(isShowBases(value.ingredientList)){
                    getBase(value.ingredientList);
                }

                return;
            }
        });
    };

    $scope.countOrdered = function(count){
        $scope.orderedCount = count;
    };

    $scope.setOrderedCount = function(){
        $scope.orderedCount = 0;
    };

});