function PbTableCtrl($scope) {

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
    
    $scope.action = function(row) { 
        let url = "";
        debugger;
        if(row.name === "Confirmación de apoyo asignado"){
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/confirmacion_apoyo/content/?_l=es&app=aspiranteSDAE&id=" + row.id;    
        } else if (row.name === "Llenado de información del aval") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/financiamiento_aval/content/?_l=es&app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Modificar información financiamiento") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/modificar_aval/content/?_l=es&app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Modificar solicitud") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/modificar_solicitud_apoyo/content/?app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Llenado solicitud de apoyo académico") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/nueva_solicitud_SDAE/content/?tipoMoneda=" + "&id=" + row.id;
        } else if (row.name === "Aceptación de financiamiento") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/SDAEAceptacionFinanciamiento/content/?_l=es&app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Confirmación de notificación de apoyo") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/aceptacionApoyo/content/?_l=es&app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Modificar solicitud de financiamiento") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/modificar_aval/content/?_l=es&app=aspiranteSDAE&id=" + row.id;
        } else if (row.name === "Pago de estudio socio-económico") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/pago_estudio/content/?_l=es&app=aspiranteSDAE&id=" + row.id + "&caseId=" + row.caseId;
        } else if (row.name === "Esperar pago de estudio socio-económico") {
            url = window.location.protocol + "//" + window.location.hostname + "/portal/resource/app/aspiranteSDAE/pago_estudio/content/?_l=es&app=aspiranteSDAE&id=" + row.id + "&caseId=" + row.caseId;
        } 
        
        window.location.replace(url);
    };   
}