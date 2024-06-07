EHR.model.DataModelManager.registerMetadata('ParentChild', {
    allQueries: {
        Id: {
            inheritFromParent: true,
            editable: false,
            hidden: true,
            columnConfig: {
                editable: false
            }
        },
        caseid: {
            inheritFromParent: true,
            editable: false,
            hidden: true,
            columnConfig: {
                editable: false
            }
        }
    }
});
