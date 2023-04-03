function($scope, $http) {
    $scope.formatoFecha = function (fechaString) {
        const fecha = new Date(fechaString);
        const dia = fecha.getDate().toString().padStart(2, '0');
        const mes = (fecha.getMonth() + 1).toString().padStart(2, '0');
        const anio = fecha.getFullYear().toString();
        
        return `${dia}/${mes}/${anio}`;
    }
    
    $scope.formatoHora = function (fechaString) {
        const hora = new Date(fechaString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        return hora;
    }
    
}