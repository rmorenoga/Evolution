function logCPG(Amp,Off,Ph,freq,tstep,file)

	local logtable = {
		time = tstep,
		amplitude = Amp,
		offset = Off,
		phasediff = {},
		v = freq,
	}

	for k in pairs(Ph) do logtable['phasediff'][k] = Ph[k] end
	--print('***********************')
	--for k in pairs(logtable) do print(logtable[k]) end
	local str = json.encode (logtable)
	file:write(str)
	file:write(",")
	--print(str)
end
