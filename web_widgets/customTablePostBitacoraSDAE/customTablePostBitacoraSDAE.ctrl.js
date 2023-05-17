function PbTableCtrl($scope, $http, $window,blockUI, modalService) {
    var ctrl = this;
    this.isArray = Array.isArray;
    this.orden = true;
    
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
        blockUI.start();
        var datos = angular.copy($scope.properties.dataToSend);

        var req = {
            method: method,
            url: url,
            data: datos,
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                console.log(data.data)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }


$scope.lstCampus = [];
    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
  $scope.setOrderBy = function(order) {
      if ($scope.properties.dataToSend.orderby == order) {
          $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
      } else {
          $scope.properties.dataToSend.orderby = order;
          $scope.properties.dataToSend.orientation = "ASC";
      }
      doRequest("POST", $scope.properties.urlPost);
  }

  $scope.filterKeyPress = function(columna, press) {
      var aplicado = true;
      for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
          const element = $scope.properties.dataToSend.lstFiltro[index];
          if (element.columna == columna) {
              $scope.properties.dataToSend.lstFiltro[index].valor = press;
              $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
              aplicado = false;
          }

      }
      if (aplicado) {
          var obj = { "columna": columna, "operador": "Que contengan", "valor": press }
          $scope.properties.dataToSend.lstFiltro.push(obj);
      }

      doRequest("POST", $scope.properties.urlPost);
  }


  $scope.lstPaginado = [];
  $scope.valorSeleccionado = 1;
  $scope.iniciarP = 1;
  $scope.finalP = 10;
  $scope.valorTotal = 10;

  $scope.loadPaginado = function() {
      $scope.valorTotal = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
      $scope.lstPaginado = []
      if ($scope.valorSeleccionado <= 5) {
          $scope.iniciarP = 1;
          $scope.finalP = $scope.valorTotal > 10 ? 10 : $scope.valorTotal;
      } else {
          $scope.iniciarP = $scope.valorSeleccionado - 5;
          $scope.finalP = $scope.valorTotal > ($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
      }
      for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {

          var obj = {
              "numero": i,
              "inicio": ((i * 10) - 9),
              "fin": (i * 10),
              "seleccionado": (i == $scope.valorSeleccionado)
          };
          $scope.lstPaginado.push(obj);
      }
  }

  $scope.siguiente = function() {
      var objSelected = {};
      for (var i in $scope.lstPaginado) {
          if ($scope.lstPaginado[i].seleccionado) {
              objSelected = $scope.lstPaginado[i];
              $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
          }
      }
      $scope.valorSeleccionado = $scope.valorSeleccionado + 1;
      if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.properties.dataToSend.limit)) {
          $scope.valorSeleccionado = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
      }
      $scope.seleccionarPagina($scope.valorSeleccionado);
  }

  $scope.anterior = function() {
      var objSelected = {};
      for (var i in $scope.lstPaginado) {
          if ($scope.lstPaginado[i].seleccionado) {
              objSelected = $scope.lstPaginado[i];
              $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
          }
      }
      $scope.valorSeleccionado = $scope.valorSeleccionado - 1;
      if ($scope.valorSeleccionado == 0) {
          $scope.valorSeleccionado = 1;
      }
      $scope.seleccionarPagina($scope.valorSeleccionado);
  }

  $scope.seleccionarPagina = function(valorSeleccionado) {
      var objSelected = {};
      for (var i in $scope.lstPaginado) {
          if ($scope.lstPaginado[i].numero == valorSeleccionado) {
              $scope.inicio = ($scope.lstPaginado[i].numero - 1);
              $scope.fin = $scope.lstPaginado[i].fin;
              $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
              $scope.properties.dataToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.properties.dataToSend.limit)
          }
      }

      doRequest("POST", $scope.properties.urlPost);
  }
  $scope.getCampusByGrupo = function(campus) {
      var retorno = "";
      for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
          if (campus == $scope.properties.lstCampus[i].grupoBonita) {
              retorno = $scope.properties.lstCampus[i].descripcion
              if ($scope.lstCampusByUser.length == 2) {
                  $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupoBonita
              }
          } else if (campus == "Todos los campus") {
              retorno = campus
          }
      }
      return retorno;
  }

  $scope.lstMembership = [];
  $scope.$watch("properties.userId", function(newValue, oldValue) {
      if (newValue !== undefined) {
          var req = {
              method: "GET",
              url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
          };

          return $http(req)
              .success(function(data, status) {
                  $scope.lstMembership = data;
                  $scope.campusByUser();
              })
              .error(function(data, status) {
                  console.error(data);
              })
              .finally(function() {});
      }
  });

  $scope.lstCampusByUser = [];
  $scope.campusByUser = function() {
      var resultado = [];
      // var isSerua = true;
      resultado.push("Todos los campus")
      for (var x in $scope.lstMembership) {
          if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
              let i = 0;
              resultado.forEach(value => {
                  if (value == $scope.lstMembership[x].group_id.name) {
                      i++;
                  }
              });
              if (i === 0) {
                  resultado.push($scope.lstMembership[x].group_id.name);
              }
          }
      }
      // if(isSerua){
      //     resultado.push("Todos los campus")
      // }
      $scope.lstCampusByUser = resultado;
  }
  $scope.filtroCampus = ""
  $scope.addFilter = function() {
      if ($scope.filtroCampus != "Todos los campus") {
          var filter = {
              "columna": "CAMPUS",
              "operador": "Igual a",
              "valor": $scope.filtroCampus
          }
          if ($scope.properties.dataToSend.lstFiltro.length > 0) {
              var encontrado = false;
              for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                  const element = $scope.properties.dataToSend.lstFiltro[index];
                  if (element.columna == "CAMPUS") {
                      $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                      $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                      $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroCampus;
                      for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                          if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                              $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                          }
                      }
                      encontrado = true
                  }
              }

              if (!encontrado) {
                  $scope.properties.dataToSend.lstFiltro.push(filter);
                  for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                      if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                          $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                      }
                  }
              }
          } else {
              $scope.properties.dataToSend.lstFiltro.push(filter);
              for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                  if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                      $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                  }
              }
          }
      } else {

          if ($scope.properties.dataToSend.lstFiltro.length > 0) {
              var encontrado = false;
              for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                  const element = $scope.properties.dataToSend.lstFiltro[index];
                  if (element.columna == "CAMPUS") {
                      $scope.properties.dataToSend.lstFiltro.splice(index, 1);
                      $scope.properties.campusSeleccionado = null;
                  }
              }
          }

      }

  }
  $scope.sizing = function() {
      $scope.lstPaginado = [];
      $scope.valorSeleccionado = 1;
      $scope.iniciarP = 1;
      $scope.finalP = 10;
      try {
          $scope.properties.dataToSend.limit = parseInt($scope.properties.dataToSend.limit);
      } catch (exception) {

      }

      doRequest("POST", $scope.properties.urlPost);
  }

  $scope.getCatCampus = function() {
      var req = {
          method: "GET",
          url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
      };

      return $http(req)
          .success(function(data, status) {
              $scope.lstCampus = [];
              for (var index in data) {
                  $scope.lstCampus.push({
                      "descripcion": data[index].descripcion,
                      "valor": data[index].grupoBonita
                  })
              }
          })
          .error(function(data, status) {
              console.error(data);
          });
  }

  $scope.getCatCampus();
    
    $scope.sliceTexto = function(texto){
        let array = texto.match(/(.|[\r\n]){1,30}/g);
        return array;
    }
    $scope.bitacora = "";
    $scope.changeBitacora = function(){
        $scope.properties.bitacoraSelected = $scope.bitacora;
    }
    
    $scope.sliceTexto = function(texto){
        let array = texto.match(/.{1,40}/g);
        return array;
    }
    $scope.UsuarioPasoLista = function(usuario ){
        usuario ??= "";
        let txt = usuario.length <1?"N/A":usuario;
        return txt;
    }
    
    $scope.fechaFormateo = function(fecha){
        fecha ??= "";
        let txt = fecha.length <1?"N/A":fecha.slice(0,10);
        return txt;
    }
    
    $scope.paseLista = function(asistencia,acreditado,cb){
        let txt =(asistencia === 't'?"Asistencia":(acreditado === 't'?"Acreditado": (cb ==='t'?"Exento":"Sin asistencia") ) );
        return txt;
    }
    
    $scope.firstIndex=0;
    $scope.calcularRowspan = function(calcular,nombre){
        let rowspan = 0;
        if(calcular === 0 && nombre == "nombresesion"){
            rowspan = 3;
        }else if(calcular % 3 === 0 && nombre == "nombresesion" && rowspan == 0){
            rowspan = 3;
        }else if(rowspan == 0){
            rowspan = 1;
        }
        
        return rowspan;
    }
    

    $scope.getResponsableEntrevista = function(responsabledisponible){
        responsabledisponible ?? = ""
        if( responsabledisponible.length < 1){
             swal("¡Psicologo asignado!","Sin asignar (Acreditado)","")
        }else{
            var req = {
                method: "GET",
                url: `../API/extension/AnahuacRestGet?url=getResponsableEntrevista&p=0&c=10&responsabledisponible=${responsabledisponible}`
            };
            return $http(req).success(function (data, status) {
                
                swal("¡Psicologo asignado!",data.data[0].responsablesnombre,"")
            })
            .error(function (data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function () { });
        }
    }
    
    $scope.getResponsables = function(prueba){
        var req = {
            method: "GET",
            url: `../API/extension/AnahuacRestGet?url=getResponsablesPrueba&p=0&c=10&pruebaid=${prueba}`
        };
        return $http(req).success(function (data, status) {
            $scope.properties.responsables = data.data;
            closeModal();
            openModal($scope.properties.modalName);
        })
        .error(function (data, status) {
        }).finally(function () { });
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }
    
    
}