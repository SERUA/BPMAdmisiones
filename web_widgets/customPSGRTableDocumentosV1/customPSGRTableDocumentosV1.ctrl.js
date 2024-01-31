function PbTableCtrl($scope, modalService, $http) {

    this.isArray = Array.isArray;
  
    this.isClickable = function () {
      return $scope.properties.isBound('selectedRow');
    };
  
    this.selectRow = function (row) {
      if (this.isClickable()) {
        $scope.properties.selectedRow = row;
      }
    };
  
    this.isSelected = function(row) {
      return angular.equals(row, $scope.properties.selectedRow);
    }
    
      $scope.$watch("properties.content", function(){
          debugger;
          if(!$scope.properties.content){
              $scope.properties.content.push({"documento":{
                  "descripcion": "Documento 1",
              }})
          }   
      });
    
      $scope.archivoSeleccionado = null;
      $scope.resultadoCarga = '';
  
      $scope.activarInputArchivo = function (index) {
        console.log("activarInputArchivo() -> valor: " + index)
          document.getElementById('inputArchivo' + index.toString()).click();
      };
      
      $scope.tamano_documento = "";
      // Controlador para manejar la selección del archivo
      $scope.$watch('archivoSeleccionado', function (nuevoArchivo, antiguoArchivo) {
          if (nuevoArchivo !== antiguoArchivo) {
              if (nuevoArchivo) {
                  $scope.documetObject = {
                      "b64": contenidoBase64,
                      "filename": "",
                      "filetype": "",
                      "contenedor": ""
                  }
                  var lector = new FileReader();
                  
                  lector.onload = function (evento) {
                      $scope.documetObject["filename"] = event.target.files[0].name;
                      $scope.documetObject["filetype"] = event.target.files[0].type;
                      $scope.documetObject["contenedor"] = "privado";
                      
                      var binaryData = evento.target.result;
                      var base64String = window.btoa(binaryData);
                      
                      $scope.documetObject["b64"] = $scope.documetObject["filetype"] +  "," +  base64String;
                      
                      $http.post("../API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=0", $scope.documetObject )
                      .then(function (respuesta) {
                          debugger;
                          $scope.properties.content[0].url_azure = respuesta[0];
                      })
                      .catch(function (error) {
                          swal("Algo ha fallado", error.error, "error");
                      });
                  };
                  
                  lector.readAsDataURL(nuevoArchivo);
              }
          }
      });
      
      // ------------
      
    $scope.openModalFile = function(row){
        // Mejorar esto
        $scope.downloadFile(row);
        openModal($scope.properties.idModal);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    $scope.downloadFile = function(row){
        let obj = {
            "linkSource": row.url_azure,
            "fileName":  row.documento.nombre_documento,
            "extension": ".pdf",
        };
        var req = {
            method: "GET",
            url: "../API/extension/posgradosRestGet?url=getB64FileByUrlAzure&p=0&c=100&urlAzure="+ row.url_azure
            //"../API/extension/AnahuacBecasRestGET?url=getB64FileByUrlAzure&p=0&c=100&urlAzure=" + row.url_azure
        };

        return $http(req)
        .success(function (data, status) {
            if(data.data){
                obj.linkSource = data.data[0].b64;
                $scope.properties.selectedFile = obj;
            } else {
                obj.linkSource = data[0].b64;
                $scope.properties.selectedFile = obj;
            }
        })
        .error(function (data, status) {
        })
    };      
  }
  