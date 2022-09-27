

EHR.model.DataModelManager.registerMetadata('Default', {
    byQuery: {
        'study.MHC': {
            QCState: {
                getInitialValue: function(v){
                    var qc;
                    if (!v && EHR.Security.getQCStateByLabel('Completed'))
                        qc = EHR.Security.getQCStateByLabel('Completed').RowId;
                    return v || qc;
                },
            }
        }
    }
});