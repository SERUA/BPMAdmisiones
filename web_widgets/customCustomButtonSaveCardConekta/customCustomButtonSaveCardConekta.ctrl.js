function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
    
    var vm = this;

    this.action = function action() {
        // $scope.execute();
        $scope.validateCard();
    };
    $scope.tokenParams = {};
    $scope.tokenId = "";
    
    $scope.showModal = function(){
        $("#loading").modal("show");
    };
    
    $scope.hideModal = function(){
        $("#loading").modal("hide");
    };
    
    $scope.validateCard = function(){
        if($scope.properties.validateCardFormat.isValid){
            $scope.execute();
        } else {
            swal("Atención", $scope.properties.validateCardFormat.message, "warning");
        }
    };
    
    $scope.execute = function () {
        $scope.showModal();
        $scope.tokenParams = {
            "card": {
              "number": $scope.properties.dataToSend.cardNumber,
              "name":  $scope.properties.dataToSend.cardholder,
              "exp_year": "20" + $scope.properties.dataToSend.expireYear,
              "exp_month": $scope.properties.dataToSend.expireMonth,
              "cvc": $scope.properties.dataToSend.cvv,
              "address": $scope.properties.dataToSend.address
            }
        };
        Conekta.setPublicKey("key_BKn3nrrQJGw1qybfcirDprg");
        Conekta.Token.create($scope.tokenParams, successResponseHandler, errorResponseHandler);
    }
    
    var successResponseHandler = function (token) {
        $scope.hideModal();
        $scope.properties.dataFromSuccess = token.id;
        $scope.properties.cardToken = token.id;
        let data = angular.copy($scope.properties.objectCard);
        data.idToken = token.id;
        
        doRequest(data, null);
    };
    
    var errorResponseHandler = function (error) {
        $scope.hideModal();
        swal("Error", error.message_to_purchaser, "warning");
    };
    
    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequest(data, params) {
        vm.busy = true;
        var req = {
            method: "POST",
            url: $scope.properties.url,
            data: data,
            params: params
        };

        return $http(req)
        .success(function(data, status) {
            if(data.success){
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                $scope.properties.paymentInfoCard = data
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            } else {
                swal("Error", data.error, "error");
            }
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
        })
        .finally(function() {
            $scope.hideModal();
            vm.busy = false;
        });
    }
}
