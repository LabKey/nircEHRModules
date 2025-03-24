EHR.model.DataModelManager.registerMetadata('ObsDefaults', {
    byQuery: {
        'study.clinical_observations': {
            category: {
                lookup: {
                    columns: 'value,description',
                    filterArray: []
                }
            },
        },
        'study.observation_order': {
            category: {
                lookup: {
                    columns: 'value,description',
                    filterArray: []
                }
            }
        }
    }
});