Ext4.onReady(function() {
    // this is to skip inserting/updating into assignment dataset from this form
    if (EHR.data.DataEntryClientStore) {
        Ext4.override(EHR.data.DataEntryClientStore, {
            getExtraContext: function(){
                return {
                    skipCreateAssignmentRecord: {'form': 'assignment'}
                }
            }
        });
    }
});

EHR.model.DataModelManager.registerMetadata('Assignment', {
    allQueries: {
        endDate: {
            hidden: true
        }
    },
    byQuery: {
        'study.assignment': {
            'project': {
                xtype: 'combo',
                nullable: false,
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            }
        },
        'study.protocolAssignment': {
            'project': {
              hidden: true
            },
            'protocol': {
                xtype: 'combo',
                nullable: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        }
    }
});