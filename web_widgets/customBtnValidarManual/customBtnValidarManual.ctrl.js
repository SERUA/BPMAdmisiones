function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

    'use strict';
  
    var vm = this;
    $scope.final = [];
    $scope.lstBanner = [];
      $scope.myFunc = function() {
        $scope.lstBanner = {'IDBANNER':"", "FECHA":"","IDSESION":""};
        $scope.final = [];
        let paso = validacion($scope.properties.value);
        if(paso){
            $scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${$scope.properties.value['IDBANNER']}'`;
            $scope.lstBanner.FECHA += `${$scope.lstBanner.FECHA.length>0?",":""}'${formatDate($scope.properties.value.fechaExamen)}'`;
            $scope.lstBanner.IDSESION += `${$scope.lstBanner.IDSESION.length>0?",":""}'${$scope.properties.value['IdSesion']}'`;
            doRequest2("POST",$scope.properties.urlValidar,$scope.lstBanner).then(function() {
                if($scope.final.length > 0){
                    isKp($scope.properties.value)
                    $scope.final[0].fechaExamen = formatDate($scope.properties.value.fechaExamen)
                    $scope.final[0].username = $scope.properties.username;
                    doRequest("POST",$scope.properties.urlPost,$scope.final)
                }
                
            }); 
            
        }
      };
      
      function formatDate(date) {
          let current_datetime = date;
          let formatted_date = appendLeadingZeroes(current_datetime.getDate())+ "/" + appendLeadingZeroes(current_datetime.getMonth() + 1) + "/" +  current_datetime.getFullYear() ;
          return formatted_date;
      }
      
      function appendLeadingZeroes(n){
          if(n <= 9){
              return "0" + n;
          }
          return n
      }
      
      function validacion(datos){
            var error = "";
            let valores1= ["MLEX","CLEX","HLEX"];
            let valores2= ["PAAN","PAAV","PARA"];
            if(datos !== null && datos !== undefined){
                let columnas = $scope.properties.revisar;
                for(let i = 0; i< $scope.properties.revisar.length; i++){
                  if(isNullOrUndefined(datos[columnas[i]]) && columnas[i] !="fechaExamen" && columnas[i] !="tipoExamen" && (!valores2.includes(columnas[i]) && !valores1.includes(columnas[i]) )){
                    swal("¡Aviso!",`¡Debes ingresar el valor de\xa0${columnas[i]}!`,"warning")
                    return false;
                  }else if(datos.tipoExamen == "KP" && valores1.includes(columnas[i]) && isNullOrUndefined(datos[columnas[i]])){
                    swal("¡Aviso!",`¡Debes ingresar el valor de\xa0${columnas[i]}!`,"warning")
                    return false;
                  }else if(datos.tipoExamen != "KP" && valores2.includes(columnas[i]) && isNullOrUndefined(datos[columnas[i]])){
                    swal("¡Aviso!",`¡Debes ingresar el valor de\xa0${columnas[i]}!`,"warning")
                    return false;
                  }else if(datos.tipoExamen == "KP" && valores1.includes(columnas[i]) && !isRangoValue(datos[columnas[i]],0,690)){
                      swal("¡Aviso!",`${columnas[i]}\xa0(${datos[columnas[i]]})\xa0tiene que estar en el rango de 0-690`,"warning")
                      return false;
                  }else if(datos.tipoExamen != "KP" && valores2.includes(columnas[i]) && !isRangoValue(datos[columnas[i]],200,800)){
                      if(columnas[i] == "PARA" && datos[columnas[i]] != "000"){
                        swal("¡Aviso!",`${columnas[i]}\xa0(${datos[columnas[i]]})\xa0tiene que estar en el rango de 200-800 o ser 000`,"warning")
                        return false;
                      }else if(columnas[i] != "PARA"){
                        swal("¡Aviso!",`${columnas[i]}\xa0(${datos[columnas[i]]})\xa0tiene que estar en el rango de 200-800`,"warning")
                        return false; 
                      }
                      
                  }else if(columnas[i] == "fechaExamen" ){
                    if(isNullOrUndefined(datos[columnas[i]])){
                        swal("¡Aviso!",`¡Debes ingresar el valor de fecha del examen!`,"warning");
                        return false;
                    }else if(!moment(datos[columnas[i]],'DD/MM/YYYY').isValid()){
                         swal("¡Aviso!",`¡la fecha del examen no es valida tiene que ser DD/MM/YYYY!`,"warning");
                         return false;
                    }
                  }else if(columnas[i] == "tipoExamen" && isNullOrUndefined(datos[columnas[i]]) ){
                    swal("¡Aviso!",`¡Debes ingresar el valor de tipo de examen!`,"warning");
                    return false;
                  }
                }
                if(datos.tipoExamen == "KP"){
                    let valores3=["LA1","LA2","LA3","LA4","PG1","PG2","PG3","PG4","PG5","PV1","PV4","LEO1","LEO3","LEO4","LEO5","CIT1","CIT2","HI1","HI2","HI3","HI4","HI5","HI6"]
                    for(let i=0; i<valores3.length; i++){
                        
                        if(!isRangoValue(datos[valores3[i]],0,10)){
                            swal("¡Aviso!",`${valores3[i]}\xa0(${datos[valores3[i]]})\xa0tiene que estar en el rango de 0-10`,"warning")
                            return false; 
                        }else if(!haveZero(datos[valores3[i]])){
                            swal("¡Aviso!",`${valores3[i]}\xa0(${datos[valores3[i]]})\xa0tiene que estar entre 0-10 a dos dígitos`,"warning")
                            return false; 
                        }
                    }
                }
                
                
                return true;
            }
          return false;
       }
      
      function isNullOrUndefined(dato){
          if(dato === undefined || dato === null || dato.length <= 0 ){
              return true;
          }
          return false
      }
      
      function isRangoValue(value,min,max){
          if(isNullOrUndefined(value)){
              return false;
          }
          if(value >= min && value <= max){
              return true;
          }
          return false;
      }

      function haveZero(value){
        if(value.includes("0") && value.length == 2){
            return true;
        }
        return false;
      }
      
      
      function doRequest(method, url,datos) {
          blockUI.start();
          var req = {
              method: method,
              url: url,
              data: angular.copy(datos)
          };
          return $http(req)
              .success(function (data, status) {
                      //swal('¡Se han guardado los datos correctamente!',"","success")
                      $scope.properties.tabla = "tabla";
                      $scope.properties.update = "";
                      //doRequest3("POST","/bonita/API/extension/AnahuacRest?url=subirDatosBannerEthos&p=0&c=100",$scope.final)
              })
              .error(function (data, status) {
                let fecha = `${datos[key].slice(3,5)}/${datos[key].slice(0,2)}/${datos[key].slice(6,10)}`;
                $scope.final[0].fechaExamen = new Date(fecha)
              })
              .finally(function () {
                  
                  blockUI.stop();
              });
      }


    function doRequest2(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                revisarDatos(data,$scope.properties.value)
            })
            .error(function (data, status) {
            })
    }
    
    function doRequest3(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                console.log("Se subieron los datos");
            })
            .error(function (data, status) {
            })
    }


    function revisarDatos(data,datos){
        data.data.forEach( (info,index) =>{
            if(!info.Existe){
                swal("¡Aviso!",`¡Id banner incorrecto o no se encuentra!`,"warning")
            }
            else if(info.mismaFecha && !$scope.properties.value.update ){
                swal("¡Aviso!",`¡El aspirante ya tiene puntuación en la fecha ${formatDate(datos['fechaExamen'])}`,"warning");
            }
            /*else if(!info.EstaEnCarga){
                swal("¡Aviso!",`¡El aspirante no se encuentra en carga y consulta de resultados!`,"warning");
            }*/
            else if(!info.puede && !$scope.properties.value.update){
                swal("¡Aviso!",`¡El aspirante ya cuenta con una puntuacion!`,"warning");
            }
            else{
                //hacer la conversion segun la tabla y guardar los valores originales para mostrar
                if(datos.update != false){
                    datos.caseId = info.caseId;    
                }
                $scope.final = [ ...$scope.final,datos]
            }
        })
        
    }
    
    function findData(valor){
        let index = $scope.properties.value.findIndex(function(item,i){
           return item.IDBANNER === valor
        });
        return  index
    }    


    function isKp(datos){
        if(datos.tipoExamen != "KP"){
            let valores = ["MLEX","CLEX","HLEX","CIT1","CIT2","HI1","HI2","HI3","HI4","HI5","HI6","LA1","LA2","LA3","LA4","LEO1","LEO3","LEO4","LEO5","PG1","PG2","PG3","PG4","PG5","PV1","PV4"]
            valores.forEach(element =>{
                datos[element] = "";
            });
        }
    }
      
  
  }
  