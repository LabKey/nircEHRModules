
EHR.model.DataModelManager.registerMetadata('Arrival', {
    allQueries: {

    },
    byQuery: {
        'study.arrival': {
            'Id/demographics/dam': {
                xtype: 'ehr-animalfield'
            },
            'Id/demographics/sire': {
                xtype: 'ehr-animalfield'
            },
            attachmentFile: {
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            sourceFacility: {
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        }
    }
});