
Ext4.define('NIRC_EHR.data.CaseClientStore', {
    extend: 'EHR.data.DataEntryClientStore',

    getExtraContext: function(){
        // Submit all of the weights in this batch as a separate property on the extra context,
        // so that server-side validation can see them all at once for validation purposes (and not just rely
        // on the most recent weight entry that's already been saved to the database)
        var caseMap = {};
        var allRecords = this.getRange();
        for (var idx = 0; idx < allRecords.length; ++idx){
            var record = allRecords[idx];
            if (record.get('caseid')){
                var id = record.get('Id');
                var date = record.get('date');
                if (!id || !date)
                    continue;

                date = Ext4.Date.format(date, LABKEY.extDefaultDateFormat);

                if (!caseMap[id])
                    caseMap[id] = [];

                caseMap[id].push({
                    objectid: record.get('objectid'),
                    date: date,
                    qcstate: record.get('QCState'),
                    caseid: record.get('caseid'),
                    enddate: record.get('enddate')
                });
            }
        }

        if (!LABKEY.Utils.isEmptyObj(caseMap)){
            caseMap = Ext4.encode(caseMap);

            return {
                casesInTransaction: caseMap
            }
        }

        return null;
    }
});