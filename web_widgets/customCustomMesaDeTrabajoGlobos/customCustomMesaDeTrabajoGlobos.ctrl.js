function($scope, $http, blockUI) {
    $scope.isAdmisiones = false;
    $scope.isPsicologoSup = false;
    $scope.isPsicologo = false;
    $scope.isPaseDeLista = false;
    $scope.isTiCampus = false;
    $scope.isTiSerua = false;
    $scope.isSerua = false;
  
    $scope.hasRole = function(){
        let output = false;
        if($scope.isAdmisiones){
            output = true;  
        } else if($scope.isPsicologoSup){
            output = true;  
        } else if($scope.isPsicologo){
            output = true;  
        } else if($scope.isPaseDeLista){
            output = true;  
        } else if($scope.isTiCampus){
            output = true;  
        } else if($scope.isTiSerua){
            output = true;  
        } else if($scope.isSerua){
            output = true;  
        }
        
        return output;
    }
  
  $scope.redirect = function (_param, filtro) {

      if (_param === "progreso") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua || $scope.isPaseDeLista ) {
              window.top.location.href = "/bonita/apps/administrativo/solicitudesEnProgreso/";
          }
      } else if (_param === "nuevas") {

          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/nuevasSolicitudes/";
          }

      } else if (_param === "aspirantesEnProceso") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua || $scope.isPaseDeLista ) {
              window.top.location.href = "/bonita/apps/administrativo/Aspirantes_ProcesoM/?estatusSelected=" + filtro;
          }
      } else if (_param === "sesiones") {
          if ($scope.isAdmisiones || $scope.isPsicologo || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/sesiones/";
          }
      } else if (_param === "autodescripcion") {
          if ($scope.isPsicologo || $scope.isTiSerua || $scope.isSerua || $scope.isPsicologoSup) {
              window.top.location.href = "/bonita/apps/administrativo/ReporteCitasPsicologo/";
          }
      } else if (_param === "paselista") {
          if ($scope.isAdmisiones || $scope.isPsicologo || $scope.isPaseDeLista || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/SesionCalendarizadas/";
          }
      } else if (_param === "importacionResultados") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/subir_resultados/";
          }
          //window.top.location.href = "/bonita/apps/administrativo/home/";
      } else if (_param === "generacionCartas") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/envio_cartas/";
          }
      } else if (_param === "usuariosRegistrados") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/UsuariosRegistrados/";
          }
      } else if (_param === "transferencias") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/appTransferenciasAspirantes/";
          }
      } else if (_param === "aspisantesCitaAsignada") {
          if ($scope.isAdmisiones || $scope.isPsicologo || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/Aspirantes_Proceso_Reporte_Citas/";
          }
      } else if (_param === "resultadoDeAdmision") {
          if ($scope.isAdmisiones || $scope.isTiSerua || $scope.isSerua) {
              window.top.location.href = "/bonita/apps/administrativo/resultados_de_admision/";
          }
      }
  };


  $scope.lstMembership = [];
  $scope.$watch("properties.userId", function (newValue, oldValue) {
      if (newValue !== undefined) {
          var req = {
              method: "GET",
              url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
          };

          return $http(req)
              .success(function (data, status) {
                  $scope.lstMembership = data;
                  for (var i = 0; i < $scope.lstMembership.length; i++) {
                      console.log("|" + $scope.lstMembership[i].role_id.displayName + "|");
                      if ($scope.lstMembership[i].role_id.displayName === "Admisiones") {
                          $scope.isAdmisiones = true;
                          console.log("Admisiones " + $scope.isAdmisiones);
                      } else if ($scope.lstMembership[i].role_id.displayName === "Psicólogo Supervisor ") {
                          $scope.isPsicologoSup = true;
                          console.log("isPsicologoSup " + $scope.isPsicologoSup);
                      } else if ($scope.lstMembership[i].role_id.displayName === "Psicólogo ") {
                          $scope.isPsicologo = true;
                          console.log("isPsicologo " + $scope.isPsicologo);
                      } else if ($scope.lstMembership[i].role_id.displayName === "Pase de Lista") {
                          $scope.isPaseDeLista = true;
                          console.log("isPaseDeLista " + $scope.isPaseDeLista);
                      } else if ($scope.lstMembership[i].role_id.displayName === "TI CAMPUS") {
                          $scope.isTiCampus = true;
                          console.log("isTiCampu " + $scope.isTiCampus);
                      } else if ($scope.lstMembership[i].role_id.displayName === "TI SERUA") {
                          $scope.isTiSerua = true;
                          console.log("isTiSerua " + $scope.isTiSerua);
                      } else if ($scope.lstMembership[i].role_id.displayName === "SERUA") {
                          $scope.isSerua = true;
                          console.log("isSerua " + $scope.isSerua);
                      }
                  }

              })
              .error(function (data, status) {
                  console.error(data);
              })
              .finally(function () { });
      }
  });


  function doRequest(method, url, params, dataToSend, callback) {
      blockUI.start();
      var req = {
          method: method,
          url: url,
          data: angular.copy(dataToSend),
          params: params
      };

      return $http(req).success(function (data, status) {
          callback(data)
      })
          .error(function (data, status) {
              console.error(data)
          })
          .finally(function () {

              blockUI.stop();
          });
  }
  $scope.enProceso = 0;
  $scope.nuevasSolicitudes = 0;
  $scope.esperandoPago = 0;
  $scope.autodescripcion = 0;

  $scope.AutodescripcionEnProceso = 0;
  $scope.EleccionPruebasNoCalendarizado = 0;
  $scope.NoImpresoCredencial = 0;
  $scope.YaSeImprimioSuCredencial = 0;
  $scope.initializeDatosProceso = function () {
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.enProceso = datos.totalRegistros;
      // })
      doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
          "estatusSolicitud": "Nuevas solicitudes",
          "tarea": "Validar Información",
          "lstFiltro": [],
          "type": "aspirantes_proceso",
          "orderby": "",
          "orientation": "DESC",
          "limit": 10,
          "offset": 0
      }, function (datos) {
          $scope.nuevasSolicitudes = datos.totalRegistros;
      })
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Solicitud en espera de pago",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.esperandoPago = datos.totalRegistros;
      // })
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Solicitud con pago aceptado",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.autodescripcion = datos.totalRegistros;
      // })
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Solicitud en progreso",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.solicitudProgreso = datos.totalRegistros;
      // })
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Autodescripción en proceso",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.AutodescripcionEnProceso = datos.totalRegistros;
      // });
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Elección de pruebas no calendarizado",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.EleccionPruebasNoCalendarizado = datos.totalRegistros;
      // });
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "No se ha impreso credencial",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.NoImpresoCredencial = datos.totalRegistros;
      // });
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "estatusSolicitud": "Ya se imprimió su credencial",
      //     "tarea": "Validar Información",
      //     "lstFiltro": [],
      //     "type": "aspirantes_proceso",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.YaSeImprimioSuCredencial = datos.totalRegistros;
      // });
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "tarea": "Lista roja",
      //     "estatusSolicitud" : "Solicitud lista roja",
      //     "lstFiltro": [],
      //     "type": "lista_roja",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.listaRoja = datos.totalRegistros;
      // })
      // doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", {}, {
      //     "tarea": "Solicitud rechazada",
      //     "estatusSolicitud" : "Solicitud rechazada",
      //     "lstFiltro": [],
      //     "type": "aspirantes_rechazados",
      //     "orderby": "",
      //     "orientation": "DESC",
      //     "limit": 10,
      //     "offset": 0
      // }, function(datos) {
      //     $scope.rechazados = datos.totalRegistros;
      // })
  }
  $scope.initializeDatosProceso();
}