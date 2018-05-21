function getDistance(CurrentTPart,TPoints,seqLength,position,initAngle,width,getDistanceToGoal,getDistancebyPartToGoal,shortChallenge,environmentFraction)
    local Goal = false
    local D = 0
    local goalX,goalY,goalAngle = 0

    --Track movements of the robot in the maze structure

    if(CurrentTPart >= 1) then
               
        if(CurrentTPart<=seqLength) then
            -- Check if over output point taking into account output angle
            CurrentTPart = checkOverOutputPart(TPoints[CurrentTPart],CurrentTPart,position)
	   end

        if(CurrentTPart<=seqLength) then
            -- Check if over input point taking into account input angle
           CurrentTPart = checkOverInputPart(TPoints[CurrentTPart],CurrentTPart,position)    
        end

        -- Check if robot got out of the last part
        if(CurrentTPart>seqLength) then
            Goal = true
        end

    else
        --Check if robot is over starting point
        CurrentTPart = checkOverOutput(CurrentTPart,0,0,initAngle,position)        

    end	

    --Calculate distance based on position
        
    if(CurrentTPart>=1) then

        if  not shortChallenge then
            goalX = TPoints[seqLength][3]
            goalY = TPoints[seqLength][4]
        else
            goalX,goalY,goalAngle = getGoalFromFraction(TPoints[CurrentTPart],environmentFraction)
            Goal = checkOverGoal(goalX,goalY,goalAngle,position)
        end

        if(CurrentTPart<=seqLength) then
            if getDistanceToGoal then
                D = getDistanceToGoalNormalized(goalX,goalY,position,TPoints,CurrentTPart,seqLength,getDistancebyPartToGoal)              
            else
                D = getDistanceToStartingPointNormalized(goalX,goalY,position,width)
            end
        end

    else

        if getDistanceToGoal then
            --D = getDistanceToGoalBehindZeroNormalized(goalX,goalY,position,TPoints,seqLength,getDistancebyPartToGoal)--Deprecated will always return 1
            D = 1
        else
            D = 0
        end
        --print(D)
    end

    if Goal then
        if getDistanceToGoal then
            D = 0
        else
            D = 1
        end
    end
    --print(D,CurrentTPart,Goal,shortChallenge)
    return CurrentTPart,Goal,D
end

function checkOverGoal(goalX,goalY,goalAngle,position)

    local ret = checkOverOutput(1,goalX,goalY,goalAngle,position)
    if ret > 1 then
        return true
    else
        return false
    end
end

function checkOverOutputPart(TPart,CurrentPart,position)
    local outputX = TPart[3]
    local outputY = TPart[4]
    local outputAngle = TPart[6]
    return checkOverOutput(CurrentPart,outputX,outputY,outputAngle,position)
end

function checkOverOutput(CurrentPart,outputX,outputY,outputAngle,position)
    local CurrentP = CurrentPart
    local positionX = position[1]
    local positionY = position[2]

    if(outputAngle == 0) then
        if(positionY > outputY) then
            CurrentP = CurrentP + 1
        end
    elseif(outputAngle == math.pi/2) then
        if(positionX < outputX) then
            CurrentP = CurrentP + 1
        end
    elseif(outputAngle == -math.pi/2) then
        if(positionX > outputX) then
            CurrentP = CurrentP + 1
        end
    elseif(outputAngle == math.pi or outputAngle == -math.pi) then
        if(positionY < outputY) then
            CurrentP = CurrentP + 1
        end
    end

    return CurrentP

end

function checkOverInputPart(TPart,CurrentPart,position)
    local inputX = TPart[1]
    local inputY = TPart[2]
    local inputAngle = TPart[5]
    return checkOverInput(CurrentPart,inputX,inputY,inputAngle,position)
end


function checkOverInput(CurrentPart,inputX,inputY,inputAngle,position)
    local CurrentP = CurrentPart  
    local positionX = position[1]
    local positionY = position[2]

    if(inputAngle == 0) then
        if(positionY < inputY) then
            CurrentP = CurrentP - 1
        end
    elseif(inputAngle == math.pi/2) then
        if(positionX > inputX) then
            CurrentP = CurrentP - 1
        end
    elseif(inputAngle == -math.pi/2) then
        if(positionX < inputX) then
            CurrentP = CurrentP - 1
        end
    elseif(inputAngle == math.pi or inputAngle == -math.pi) then
        if(positionY > inputY) then
            CurrentP = CurrentP - 1
        end
    end

    return CurrentP

end

function getGoalFromFraction(TPart,environmentFraction)
    local shape = TPart[8]
    local outputAngle = TPart[6]
    local outputX = TPart[3]
    local outputY = TPart[4]
    local inputAngle = TPart[5]
    local inputX = TPart[1]
    local inputY = TPart[2]
    local outAngle = 0

    if (shape == 's' or shape == 'b') then
        if (outputAngle == 0 or outputAngle == math.pi or outputAngle == -math.pi) then
            goalX = outputX
            goalY = outputY*environmentFraction
        elseif (outputAngle == math.pi/2 or outputAngle == -math.pi/2) then
            goalX = outputX*environmentFraction
            goalY = outputY
        end
        outAngle = outputAngle
    elseif (shape == 'l' or shape == 'r') then
        --TODO Implement for shapes l and r
        goalX = outputX
        goalY = outputY
    end

    return goalX,goalY,outAngle

end

function getDistanceToStartingPointNormalized(goalX,goalY,position,width)
    local positionX = position[1]
    local positionY = position[2]
    local D = 0
    local totalDistance = 0
    local DN = 0

    local Dx = math.abs(0-positionX)
    local Dy = math.abs(0-positionY)
    D = Dx + Dy

    --Get total distance based on goal
    local DxTotal = math.abs(goalX-0)
    local DyTotal = math.abs(goalY-0)
    totalDistance = DxTotal + DyTotal + width/2

    DN = D/totalDistance

    if DN > 1 then
        DN = 1
    end

    return DN 

end

function getDistanceToGoalBehindZeroNormalized(goalX,goalY,position,TPoints,seqLength,distanceByPart)
    local totalDistance = 0
    local D = 0
    local positionX = position[1]
    local positionY = position[2]

    local Dx0 =  math.abs(0-positionX)
    local Dy0 =  math.abs(0-positionY)
    local D0 = Dx0 + Dy0

    if distanceByPart then
        for i=1,seqLength,1 do
            totalDistance = totalDistance + TPoints[i][7]
        end
    else
        local DxTotal = math.abs(goalX-0)
        local DyTotal = math.abs(goalY-0)
        totalDistance = DxTotal + DyTotal
    end

    D = (D0 + totalDistance)/totalDistance

    if D > 1 then
        D = 1
    end

    return D

end 


function getDistanceToGoalNormalized(goalX,goalY,position,TPoints,CurrentPart,seqLength,distanceByPart)
    local totalSum = 0
    local D = 0
    local positionX = position[1]
    local positionY = position[2]
    local DN = 0

    if distanceByPart then
        --Get Manhattan distance based on part
        local DPart = GetPartManhattanDistance(TPoints[CurrentPart],CurrentPart,position)
        local sum  = 0
        for i=CurrentTPart+1,seqLength,1 do
            sum = sum + TPoints[i][7]
        end

        D = DPart + sum

        for i=1,seqLength,1 do
            totalSum = totalSum + TPoints[i][7]
        end

    else
        --Get Manhattan distance based on goal 
        local Dx = math.abs(goalX-positionX)
        local Dy = math.abs(goalY-positionY)
        D = Dx + Dy

        --Get total distance based on goal
        local DxTotal = math.abs(goalX-0)
        local DyTotal = math.abs(goalY-0)
        totalSum = DxTotal + DyTotal

    end

    DN = D/totalSum

    if DN > 1 then
        DN = 1
    end

    return DN

end


function GetPartManhattanDistance(TPart,CurrentPart,position)
    local shape = TPart[8]
    local inputX = TPoints[CurrentPart][1]
    local inputY = TPoints[CurrentPart][2]
    local outputX = TPart[3]
    local outputY = TPart[4]
    local outputAngle = TPart[6]
    local positionX = position[1]
    local positionY = position[2]
    local D = 0

    if (shape=='s' or shape == 'b') then
        
        local Dy = math.abs(outputY-positionY)
        local Dx = math.abs(outputX-positionX)
        D = Dx + Dy; 
        return D       

    elseif (shape =='r' or shape =='l') then
        --Take into account outputAngle
        
        --print(angle,outputX,outputY,inputX,inputY)
        if(outputAngle == 0 or outputAngle == math.pi or outputAngle == -math.pi) then
            local DyInputLine = math.abs(inputY-positionY)
            local DxOutputLine = math.abs(outputX-positionX)
            --print(angle,DyoinputLine,DxooutputLine)
            if (DyInputLine < DxOutputLine) then
                local outputLine = math.abs(outputY-inputY)
                local remainingInputLine = math.abs(outputX-positionX)
                D = outputLine + remainingInputLine + DyInputLine
                --print(outputLine,rInputLine,DyoinputLine,Do)
            else
                local remainingOutputLine = math.abs(outputY-positionY)
                D = remainingOutputLine + DxOutputLine
                --print(routputLine,DxooutputLine,Do)
            end
            return D
        elseif(outputAngle == math.pi/2 or outputAngle == -math.pi/2) then
            local DxInputLine = math.abs(inputX-positionX)
            local DyOutputLine = math.abs(outputY-positionY)
            --print(angle,Dxoinputline,Dyooutputline)
            if (DxInputLine<DyOutputLine) then
                local outputLine = math.abs(outputX-inputX)
                local remainingInputLine = math.abs(outputY-positionY)
                D = outputLine + remainingInputLine + DxInputLine 
            else
                local remainingOutputLine = math.abs(outputX-positionX)
                D = remainingOutputLine + DyOutputLine
            end
            return D  
        end  
    end
    
end

function getTPointsS(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-1*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+1*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 1 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-0.5*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+0.5*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 0.5 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         --TPoints[i][7] = xo + yo
         TPoints[i][7] = xo + yo + (Width/2) --Distance added in Manhattan distance
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        --TPoints[i][7] = -xo + yo
         TPoints[i][7] = -xo + yo + (Width/2) --Distance added in Manhattan distance
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end

function getTPoints(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-2*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+2*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 2 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-4*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+4*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 4 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         TPoints[i][7] = xo + yo
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        TPoints[i][7] = -xo + yo
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end

