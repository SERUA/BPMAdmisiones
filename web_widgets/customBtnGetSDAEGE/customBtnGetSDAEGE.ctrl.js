function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

    $scope.$watch("properties.urlParameter", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            $scope.properties.value = [];
            doRequest("AnahuacBecasRestGET?url=getExisteSDAEGestionEscolar",$scope.properties.urlParameter[0],1);           
        }
    });


    function doRequest(url,parameter,numero) {
        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/${url}&p=0&c=100${parameter}`,
        };

        return $http(req).success(function(data, status) {
            switch (numero) {
            case 1:
                if (data.length > 0) {
                    $scope.properties.value[numero-1] = data[0];
                }
                if($scope.properties.value[numero-1] == true){
                    doRequest("AnahuacBecasRestGET?url=getSDAEGestionEscolar", $scope.properties.urlParameter[numero], 2);
                }else{
                    debugger;
                    $scope.properties.value[1] = {"catgestionescolar_pid": $scope.properties.id, "manejaapoyo":true};
                    $scope.properties.value[2] = false;
                    $scope.properties.value[3] = [
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha":$scope.properties.fecha
                        },
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 1 + ""
                        }, 
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 2 + ""
                        }
                        
                    ];
                }

                break;
            case 2:
                if (data.length > 0) {
                    $scope.properties.value[numero-1] = data[0];
                    if($scope.properties.value[numero-1].manejaapoyo != null){
                        $scope.properties.value[numero-1].manejaapoyo = ($scope.properties.value[numero-1].manejaapoyo == 't'?true:false)
                        $scope.properties.value[numero-1].manejaprontopago = ($scope.properties.value[numero-1].manejaprontopago == 't'?true:false)
                    }
                }
                doRequest("AnahuacBecasRestGET?url=getExisteSDAECreditoGE",$scope.properties.urlParameter[numero].replace('[SDAEGE]',$scope.properties.value[1].persistenceid), 3);

                break;
            case 3:
                if (data.length > 0) {
                    $scope.properties.value[numero-1] = data[0];
                }
                doRequest("AnahuacBecasRestGET?url=getCreditoGE", $scope.properties.urlParameter[numero-1].replace('[SDAEGE]',$scope.properties.value[1].persistenceid), 4);

                break;
            case 4:
                if (data.length > 0) {
                    // $scope.properties.value[numero-1] = [{},{}];
                    // $scope.properties.value[numero-1][0] = data[0];
                    $scope.properties.value[3] = [{},{},{}];
                    $scope.properties.value[3][0] = data[0];
                    doRequest("AnahuacBecasRestGET?url=getCreditoGE", $scope.properties.urlParameter[numero-1].replace('[SDAEGE]', $scope.properties.value[1].persistenceid), 5);
                } else{
                    $scope.properties.value[numero-1] = [
                        {
                            "persistenceid": "",
                            "creditoenero":"", 
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha":$scope.properties.fecha
                        },
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 1 + ""
                        }
                        ,//Cambios para le tercer periodo ------------------------------------------------------------------
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 2 + ""
                        }//Cambios para le tercer periodo ------------------------------------------------------------------
                    ];
                    if( $scope.properties.value[0]){
                        $scope.properties.value[numero-1][0].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;
                        $scope.properties.value[numero-1][1].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;
                        $scope.properties.value[numero-1][2].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;//Cambios para le tercer periodo -------------------------------------------------
                    }
                }

                break;
            case 5:
                debugger;
                if (data.length > 0) {
                    // $scope.properties.value[numero-1] = [{},{}];
                    // $scope.properties.value[numero-1][1] = data[0];
                    // $scope.properties.value[3] = [{},{},{}];
                    $scope.properties.value[3][1] = data[0];
                    doRequest("AnahuacBecasRestGET?url=getCreditoGE", $scope.properties.urlParameter[numero-1].replace('[SDAEGE]', $scope.properties.value[1].persistenceid), 6);
                } else{
                    $scope.properties.value[3] = [
                        {
                            "persistenceid": "",
                            "creditoenero":"", 
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha":$scope.properties.fecha
                        },
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 1 + ""
                        }
                        ,//Cambios para le tercer periodo ------------------------------------------------------------------
                        {
                            "persistenceid": "",
                            "creditoenero":"",
                            "creditomayo":"",
                            "creditoagosto":"",
                            "creditoseptiembre":"",
                            "fecha": parseInt($scope.properties.fecha) + 2 + ""
                        }//Cambios para le tercer periodo ------------------------------------------------------------------
                    ];
                    if( $scope.properties.value[0]){
                        $scope.properties.value[3][0].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;
                        $scope.properties.value[3][1].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;
                        $scope.properties.value[3][2].sdaecatgestionescolar_pid =   $scope.properties.value[1].persistenceid;//Cambios para le tercer periodo -------------------------------------------------
                    }
                }

                // if (data.length > 0) {
                //     $scope.properties.value[3][1] = data[0];
                // }

                break;
            case 6:
                debugger;
                if (data.length > 0) {
                    $scope.properties.value[3][2] = data[0];
                } else {
                    $scope.properties.value[3][2] = {
                        "persistenceid": "",
                        "creditoenero":"0",
                        "creditomayo":"0",
                        "creditoagosto":"0",
                        "creditoseptiembre":"0",
                        "sdaecatgestionescolar_pid": $scope.properties.value[1].persistenceid,
                        "fecha": parseInt($scope.properties.fecha) + 2 + ""
                    }
                }
                break;
            } 
        }).error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        }).finally(function() {
            blockUI.stop();
        });
    }
}