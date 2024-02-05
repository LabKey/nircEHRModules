EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {

    },
    byQuery: {
        'study.birth': {
            'Id/demographics/dam': {
                xtype: 'ehr-animalfield'
            },
            'Id/demographics/sire': {
                xtype: 'ehr-animalfield'
            }
        }
    }
});