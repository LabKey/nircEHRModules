EHR.Utils.rowEditorPlugin = 'NIRC_EHR.plugin.RowEditor';

EHR.Utils.editUIButtonHandler = function(schemaName, queryName, dataRegionName, paramMap, copyFilters){
    var params = {
        schemaName: schemaName,
        'query.queryName': queryName,
        showImport: false
    };

    this.editUIButtonCore(schemaName, queryName, dataRegionName, paramMap, undefined, params);
};