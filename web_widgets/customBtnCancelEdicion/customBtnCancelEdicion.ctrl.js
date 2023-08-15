function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
      debugger;
      
      $scope.properties.actualizar = true;
      
      if($scope.properties.editarSec1 == false){
        $scope.properties.editarSec1 = true;
        
        }else if($scope.properties.editarSec1 == true){
        $scope.properties.editarSec1 = false;
        }
        if($scope.properties.editarSec2 == false){
        $scope.properties.editarSec2 = true;
        
        }else if($scope.properties.editarSec2 == true){
        $scope.properties.editarSec2 = false;
        }
        if($scope.properties.editarSec3 == false){
        $scope.properties.editarSec3 = true;
        
        }else if($scope.properties.editarSec3 == true){
        $scope.properties.editarSec3 = false;
        }
        if($scope.properties.editarSec4 == false){
        $scope.properties.editarSec4 = true;
        
        }else if($scope.properties.editarSec4 == true){
        $scope.properties.editarSec4 = false;
        }
        if($scope.properties.editarSec5 == false){
        $scope.properties.editarSec5 = true;
        
        }else if($scope.properties.editarSec5 == true){
        $scope.properties.editarSec5 = false;
        }
        if($scope.properties.editarSec6 == false){
        $scope.properties.editarSec6 = true;
        
        }else if($scope.properties.editarSec6 == true){
        $scope.properties.editarSec6 = false;
        }
        if($scope.properties.editarSec7 == false){
        $scope.properties.editarSec7 = true;
        
        }else if($scope.properties.editarSec7 == true){
        $scope.properties.editarSec7 = false;
        }
        if($scope.properties.editarSec8 == false){
        $scope.properties.editarSec8 = true;
        // Construir la URL de la API con el caseId
        const apiUrl = `../API/bdm/businessData/com.anahuac.model.ContactoEmergencias?q=findByCaseId&p=0&c=999&f=caseId=${$scope.properties.caseId}`;

        // Función para obtener datos de la API
        async function fetchDataFromAPI() {
        try {
            const response = await fetch(apiUrl);
            
            if (!response.ok) {
            throw new Error('Error al obtener los datos de la API');
            }
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error(error);
            return null;
        }
        }

        // Función para recargar los datos y actualizar la variable
        async function refreshData() {
        const updatedData = await fetchDataFromAPI();
        
        if (updatedData) {
            // Actualizar la variable con los nuevos datos
            $scope.properties.objcasosDeEmergencia = updatedData;
            
            // Realizar las acciones necesarias con los datos actualizados
            // por ejemplo, actualizar la interfaz de usuario
        }
        }

        // Llamar a la función de recarga cuando sea necesario
        refreshData();
        }else if($scope.properties.editarSec8 == true){
        $scope.properties.editarSec8 = false;
        }
  }

}