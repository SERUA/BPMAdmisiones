function PbTableCtrl($scope, $http, $window) {
    var vm = this;
    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                console.log(data.data)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $scope.asignarTarea = function (rowData) {
       var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseId}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                debugger
                var url="https://anahuac-preproduction.bonitacloud.com/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[TASKID]", data[0].id);
                window.top.location.href=url;
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    
    $scope.isenvelope=false;
    $scope.selectedrow={};
    $scope.mensaje="";
    $scope.envelope=function(row){
        $scope.isenvelope=true;
        $scope.mensaje="";
        $scope.selectedrow=row;
    }
    $scope.envelopeCancel=function(){
        $scope.isenvelope=false;
        $scope.selectedrow={};
    }
         $scope.sendMail=function(row,mensaje) {
             if(row.catCampus.grupoBonita==undefined){
                 for(var i=0; i<$scope.lstCampus.length; i++){
                     if($scope.lstCampus[i].descripcion==row.catCampus.descripcion){
                         row.catCampus.grupoBonita=$scope.lstCampus[i].valor;
                     }
                 }
             }
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
              "campus": row.catCampus.grupoBonita,
              "correo": row.correoElectronico,
              "codigo": "recordatorio",
              "isEnviar": true,
              "mensaje":mensaje
            })
        };

        return $http(req)
            .success(function(data, status) {
                
                $scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
    }
    $scope.lstCampus = [{
    "descripcion":"Anáhuac Cancún",
    "valor":"CAMPUS-CANCUN"
},
{
    "descripcion":"Anáhuac Mayab",
    "valor":"CAMPUS-MAYAB"
},
{
    "descripcion":"Anáhuac México Norte",
    "valor":"CAMPUS-MNORTE"
},
{
    "descripcion":"Anáhuac México Sur",
    "valor":"CAMPUS-MSUR"
},
{
    "descripcion":"Anáhuac Oaxaca",
    "valor":"CAMPUS-OAXACA"
},
{
    "descripcion":"Anáhuac Puebla",
    "valor":"CAMPUS-PUEBLA"
},
{
    "descripcion":"Anáhuac Querétaro",
    "valor":"CAMPUS-QUERETARO"
},
{
    "descripcion":"Anáhuac Xalapa",
    "valor":"CAMPUS-XALAPA"
},
{
    "descripcion":"Juan Pablo II",
    "valor":"CAMPUS-JP2"
},
{
    "descripcion":"Anáhuac Cordoba",
    "valor":"CAMPUS-CORDOBA"
}
];

    $scope.deleteUser = function () {
        swal("Desea regresar al aspirante a la asignación de Banner", {
            buttons: {
                cancel: "Cancelar",
                catch: {
                    text: "Aceptar",
                    value: "aceptar",
                }
            },
        })
            .then((value) => {
                switch (value) {
                    case "aceptar":
                        swal("", "Usuario eliminado correctamente", "success");
                        break;
                    default:
                        break;
                }
            });
    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function (data, status) {
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $(function () {
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
    $scope.reciclar = function (rowData) {
        swal({
            title: `¿Está seguro que desea reactivar la solicitud del aspirante ${rowData.primerNombre + " " + rowData.segundoNombre + " " + rowData.apellidoPaterno + " " + rowData.apellidoMaterno}?`,
            text: "La solicitud tendrá que ser validada",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            buttons: ["Cancelar", "Continuar"]
        })
            .then((willDelete) => {
                if (willDelete) {
                    doRequestCallback("PUT", `/bonita/API/bpm/humanTask/${rowData.taskId}`, {}, { "assigned_id": $scope.properties.userId }, function (success, rowData) {
                        doRequestCallback("POST", `/bonita/API/bpm/userTask/${rowData.taskId}/execution?assign=false`, {}, {}, function (success, rowData) {
                            window.top.location.href = `/bonita/apps/administrativo/appRechazado/`;
                            
                        }, rowData);
                    }, rowData);

                } else {

                }
            });
        
    }
    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequestCallback(method, url, params, payload, callback, extra) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(payload),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                callback(data, extra);
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () {
                vm.busy = false;
            });
    }

}