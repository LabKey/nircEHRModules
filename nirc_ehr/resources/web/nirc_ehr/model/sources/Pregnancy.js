EHR.model.DataModelManager.registerMetadata('Pregnancy', {
    allQueries: {

    },
    byQuery: {
        'study.pregnancy': {
            project: {
                hidden: true,
            },
            type: {
                hidden: true,
            },
            diagnosis: {
                hidden: true,
            },
            attachmentFile: {
                hidden: true,
            },
            result: {
                allowBlank: false,
                nullable: false,
            }
        },

    }
});